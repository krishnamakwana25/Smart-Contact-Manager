console.log("this is custom script file");

const toggleSidebar = () => {
	if($(".sidebar").is(":visible")) {
		//true
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		//false
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");		
	}
};


const search = () => {
	console.log("searching..")
	let query = $("#search-input").val();
	console.log(query);
	if(query == '')
	{
		console.log(query);
		$(".search-result").hide();
	}
	else
	{
		console.log(query);
		//sending request to server
		
		let url = `http://localhost:8080/search/${query}`;
		
		fetch(url)
		.then((response) => {
			return response.json();
		})
		.then((data) => {
			//data.....
			console.log(data);
			
			let text = `<div class = 'list-group'>`;
			data.forEach((contact) => {
				text += `<a href = '/user/${contact.cid}/particularcontact' class = 'list-group-item list-group-action'> ${contact.name} </a>`
				/*  /user/${contact.cid}/contact  */
			});
			text += `</div>`;		
			
			$(".search-result").html(text);
			$(".search-result").show();	
		});						
	}	
};


/*PAYMENT INTEGRATION*/
/*----------------------------------------------------------------------------*/
			/* first request to server to create order */
/*----------------------------------------------------------------------------*/

const paymentStart=()=>{	
	console.log("payment start");
	let amount = $("#payment_field").val();
	console.log("payment amount",amount);	
	if(amount=='' || amount==null)
	{
		swal({
			  title: "Oops..!",
			  text: "amount is required ..!",
			  icon: "info",
			});		
		return;		
	}
	
	//code
	//we will use ajax  to send request to serverto create order
	$.ajax(
		{
			url:'/user/create_order',
			data:JSON.stringify({amount:amount,info:'order_request'}),
			contentType:'application/json',
			type:'POST',
			dataType:'json',
			success:function(response){
				//invoced where success
				alert("order details : "+response);
				console.log(response);
				if(response.status == "created")
				{
					//open payment form
					alert("hy");
					let options={
						key:'rzp_test_ZzOd8m4fdvzVcr',
						amount:response.amount,
						currency:'INR',
						name:'Smart Contact Manager',
						description:'Donation',
						image:'data:image/png;base64,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',
						order_id:response.id,
						handler:function(response){
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("Payment successfull..");
							/*alert("Congrats..! Payment Successfully done..");*/
							
							updatePaymentOnServer(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							'paid'
							);
							
							
							swal({
							  title: "Good job!",
							  text: "Congrats..! Payment Successfully done..!",
							  icon: "success",
							});
						},			
						 "prefill": {
						 "name": "",
   						 "email": "",
						 "contact": ""
					     },
					     "notes": {
						 "address": "Smart Contact Manager - Donation"
					     },
					     "theme": {
						 "color": "#3399cc"
					     },
					};
					let rzp = new Razorpay(options);
					rzp.on('payment.failed', function (response){
							console.log(response.error.code);
							console.log(response.error.description);
							console.log(response.error.source);
							console.log(response.error.step);
							console.log(response.error.reason);
							console.log(response.error.metadata.order_id);
							console.log(response.error.metadata.payment_id);
							/*alert("Oops..! Payment failed..");*/
							swal({
							  title: "Failed..!",
							  text: "Oops Payment failed..!",
							  icon: "error",
							});
					});
					rzp.open();					
				}
			},
			error:function(error){
				//invoked when error
				console.log(error);
				alert("Something went wrong !!");
			}
		}
	)
};

/* updatePaymentOnServer */
function updatePaymentOnServer(payment_id,order_id,status)
{
	$.ajax(
	{
		url:'/user/update_order',
		data:JSON.stringify({
			payment_id:payment_id,
			order_id:order_id,
			status:status,
		}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){
			swal({
				  title: "Good job!",
				  text: "Congrats..! Payment Successfully done..!",
				  icon: "success",
				});
		},
		error:function(error){
			swal({
				  title: "Failed..!",
				  text: "Your payment is successful, but we did not get on server, we will contact you as sson as possible..!",
				  icon: "error",
				});
		},
	})
}