/**
 * index_network
 */

var stompClient = null;

function connect() {
	
	// 통신 시작
	var socket = new SockJS('/stomp');
	stompClient = Stomp.over(socket);
	
	stompClient.connect({}, function() {
		
		// 채팅 시작 메세지
		console.log('start');
		// var content = user + "의 채팅을 시작합니다.";
		// stompClient.send('/start', {}, JSON.stringify({"id" : user, "target" : user, "content" : content}));
		
		// 메세지를 구독
/*		stompClient.subscribe('/topic/receiveStart', function(message){
			var msg = JSON.parse(message.body);
			console.log('topic 메세지 : ' + msg);
			alert('수신 완료');
		});
		
		stompClient.subscribe('/user/queue/receiveNote', function(message){
			var msg = JSON.parse(message.body);
			alert(msg.content);
			
		});	*/
		
		// 메세지 구독 
		// 타입에 따라서 다름
		// ExSu : 교환권유
		stompClient.subscribe('/user/queue/receive', function(message){
			var msg = JSON.parse(message.body);
			console.log(msg);
			var msgType = msg.content.split(':');
			if(msgType[0] == 'ExSu') {
				var YesOrNo = confirm('교환 제안이 왔습니다. 수락하시겠습니까?');
				// 승락 시, 승락 메세지 날림
				if(YesOrNo) {
					msg = {"target" : msg.id, "content" : "O"}
					stompClient.send('/replySuggest', {}, JSON.stringify(msg));
					// 교환 모달을 염
					alert('승락했습니다. 교환 모달을 엽니다.');
					
				} else {
				  // 거절 시, 거절 메세지	
					msg = {"target" : msg.id, "content" : "X"}
					stompClient.send('/replySuggest', {}, JSON.stringify(msg));
				}
				
			} else if(msgType[0] == 'ReAc') {
				alert(msgType[1]);
				// 교환 모달을 염
				alert('상대방이 교환을 승락하였습니다. 교환 모델을 엽니다.');
				
			} else if(msgType[0] == 'ReDe') {
				alert(msgType[1]);
				return;
			}
		})
	})
}

function disconnect() {
	
	stompClient.send('/out', {}, JSON.stringify({"target" : user, "content" : user + '님의 채팅을 종료합니다.'}));
	stompClient.disconnect();
	
}

function sendNote() {
	stompClient.send('/sendNote', {}, JSON.stringify({"id" : user, "target" : "yuyu", "content" : user + "님께서 " + "yuyu님께 맞다이를 신청하셨습니다."}));
}

function test() {
	
	stompClient.send('/test', {}, JSON.stringify({"id" : user, "content" : "채팅을 시작합니다."}));
}