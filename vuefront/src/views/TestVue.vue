<template>
  <div class="container">
      <div class="OTO-con-1">
          <div class="OTO-con-2">
              <div class="OTO-con-3">
                  <blockquote class="blockquote-mypage"><b>
                  <p>담당 컨설턴트와의 <span class="highlight">1:1</span> 상담</p></b>
              </blockquote>
              </div>
          </div>
      </div>
      <div class="OTO-notice">
          📢&nbsp; 답변 시간은 컨설턴트 상황에 따라 다르게 소요되며 욕설 및 비방을 한 사용자는 경고 조치 없이 불이익을 받을 수 있습니다.
      </div>   
      <div class="OTO-con">
          <div class="chat-container scrollable-div" ref="chatContainer">
              <div v-for="(message, index) in messages" :key="index" :class="['chat-message', chattype(message.chatdiv)]">
                  <img :src="message.profileImage" :alt="message.name" class="chat-profile-image" >
                  <div class="message-info">
                      <div :class="['message-text', chattype(message.chatdiv)]">
                          {{ message.content }}
                      </div>
                      <div :class="['message-time', chattype(message.chatdiv)]">
                          {{ formatDate(message.chatdt) }}
                      </div>
                  </div>
              </div>
          </div>
      </div>
      <div class="OTO-userWrite">
          <input v-model="newMessage" @keyup.enter="sendMessage" type="text" class="user-input" placeholder="메시지를 입력하세요...">
          <button @click="sendMessage" class="send-button">전송</button>
          <input v-model="memno"  type="text" class="user-input" placeholder="회원 번호...">
          <button @click="connect" class="send-button">연결</button>
          현재 방번호 {{ this.roomnum }} {{ UIMG }}
      </div>
  </div>
</template>


<script>
import axios from "axios";

export default {
  data() {
      return {
          ws: null, // 웹소켓을 위한 변수
          newMessage: '',
          messages: [

          ],
          roomnum: null,
          memno:1,
          UIMG:'noimage.png',
          CIMG:'noimage.png',
      };
  },
  methods: {
      chattype(type){
        return type === '1' ? 'user' : 'consultant';
      },
      chatdetail(){
          const memno = new FormData();
          memno.append('memno',this.memno);
          axios.post(`${process.env.VUE_APP_BACK_END_URL}/chat/detail`,memno , { headers: {'Content-Type': 'application/json' } })
          .then((res) =>{
              this.messages = res.data;

              this.chatprofile();
              this.scrollToEnd();
          });
          
      },
      chatprofile(){
          this.messages.forEach(message => {
          if (message.chatdiv === '1') {
              message.name = 'user';
              message.profileImage = '/img/'+this.UIMG;
          } else if (message.chatdiv === '2') {
              message.name = 'consulte';
              message.profileImage = '/img/'+this.CIMG;
          }
      })
      },
      formatDate(dateString) {
          let date = new Date(dateString);
          let year = date.getFullYear();
          let month = ('0' + (date.getMonth() + 1)).slice(-2);
          let day = ('0' + date.getDate()).slice(-2);
          let hours = ('0' + date.getHours()).slice(-2);
          let minutes = ('0' + date.getMinutes()).slice(-2);

          return `${year}-${month}-${day} ${hours}:${minutes}`;
      },   
      connect() { //웹소켓 연결 시도
          this.ws = new WebSocket(`${process.env.VUE_APP_Web_Socket_URL}/ws/chat`,this.memno); // 서버 URL에 맞게 수정 필요
          this.ws.onmessage = (event) => {
          this.onMessage(event);
          };
          this.ws.addEventListener('message', (event) => {
            console.log("화긴")
              if (!this.roomnum) {
                this.roomnum=event.data;
          } 
          });
          
          

          this.ws.onopen = () => this.onOpen();
          this.ws.onerror = (error) => this.onError(error);
          this.ws.onclose = () => this.onClose();
          this.chatdetail();
      },
      sendMessage() {
        console.log(this.roomnum)
          if (this.newMessage.trim() !== '') {
              this.messages.push({ chatdiv: "1",name: 'user', profileImage: '/img/noimage.png', content: this.newMessage, chatdt: this.getCurrentDateTime()});
              const messageObject = {
                  chtno: this.roomnum,
                  content: this.newMessage,
                  chatdiv:"1"
              };
              this.ws.send(JSON.stringify(messageObject));
              
              this.newMessage = '';
              this.$nextTick(() => {
                  this.scrollToEnd();
              });
              
          };
      },
       async onMessage(event) {
        if (!this.roomnum) {
            // 초기 메시지나 roomnum 설정 메시지를 무시
            return;
            }
          console.log('Message received: ', event.data);

          const message = JSON.parse(event.data);
      
          console.log("받은메세지:",message)
          await this.messages.push({ chatdiv: "2",name: 'consulte', profileImage: '/img/noimage.png', content: message.content, chatdt: this.getCurrentDateTime()});
          this.scrollToEnd();
      },
      getCurrentDateTime() {
          const now = new Date();
          const day = now.getDate().toString().padStart(2, '0');
          const month = (now.getMonth() + 1).toString().padStart(2, '0'); // Months are 0-based
          const year = now.getFullYear();
          const hours = now.getHours().toString().padStart(2, '0');
          const minutes = now.getMinutes().toString().padStart(2, '0');
          return `${year}-${month}-${day} ${hours}:${minutes}`;
      },
      scrollToEnd() {
          const container = this.$el.querySelector('.chat-container');
          container.scrollTop = container.scrollHeight;
      },
      onOpen() { //연결 성공시
          console.log('Connected to the WebSocket server.');
          
      },
      onError(error) { //소켓 에러시
          console.error('WebSocket error:', error);
      },
      onClose() { //연결이 끊기는 경우
          console.log('Disconnected from the WebSocket server.');
      },
  },
  mounted() {
      //this.connect();
      this.scrollToEnd();
      
  },
};
</script>