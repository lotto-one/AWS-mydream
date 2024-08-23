<template>
    <div class="cvmodal-overlay">
    <div class="cvsave-background">
    <div class="cvsave-container">
        <div class="cvsave-text">이력서가 저장되었습니다.</div>
        <div class="cvsave-btn">
        <button class="cvsave-cvlist-btn" @click="cvlist()">이력서 관리</button>  
        <button class="cvsave-cvhome-btn" @click="cvhome()">홈으로</button>  
    </div>
    </div>
</div>
</div>
</template>
<script>
export default {
    methods:{
        cvlist(){
            this.$router.push('/ResumeList')
        },
        cvhome(){
            this.$router.push('/Main')
        }
    }
}
</script>
<style scoped>
.cvsave-text{
    font-size: 28px;
}
.cvsave-btn{
    margin-top: 30px;
}
.cvsave-cvlist-btn{
    background-color: #1241A4;
    color: white;
    border: none;
    margin: auto;
    height: 45px;
    width: 100px;
}
.cvsave-cvhome-btn{
    background-color: #1241A4;
    color: white;
    border: none;
    margin: auto;
    margin-left: 20px;
    height: 45px;
    width: 100px;
}


.cvsave-container {

border-radius: 10px;
  width: 400px;
  align-content: center;
  background-color: #f9fbff;
  border: 1px solid #c3dbff;
  color: black;
  height: 200px;
  margin-left:25%;

  opacity: 1; /* 불투명도를 완전히 불투명하게 설정 */
  
}
.cvmodal-overlay{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1200;
    margin:auto;
}
</style>