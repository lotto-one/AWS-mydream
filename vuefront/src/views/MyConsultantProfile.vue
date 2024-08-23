<template>
  <div class="container">
    <div class="Conpro-con-1">
      <blockquote class="blockquote-conpro">
        <b><p>나의 컨설턴트</p></b>
      </blockquote>
    </div>

    <div class="Conpro-procon" v-if="conyn">
      <div class="Conpro-procon1">
        <div class="Conimg">
          <img
            :src="`http://localhost:81/yourdream/uploads/${Consultant.imgname}`"
            :alt="'project ' + (index + 1)"
          />
        </div>
        <div class="Con-profile-section">
          <div class="project-description">
            <div class="project-title clearfix">
              <h2>[회계/재무]&nbsp;{{ Consultant.name }}</h2>
              <button class="OTO-move-btn" @click="btnmove">1:1 상담</button>
            </div>
            <div class="conpro-info">
              <div class="conpro-info-con1">
                <div class="conpro-info-int">
                  <span>Name : &nbsp;</span>{{ Consultant.name }}
                </div>
                <div class="conpro-info-int">
                  <span>Email : &nbsp;</span>{{ Consultant.email }}
                </div>
                <div class="conpro-info-int">
                  <span>경력 : &nbsp;</span>{{ Consultant.cnscareer }}
                </div>
                <div class="conpro-info-int">
                  <span>컨설팅 횟수 : &nbsp;</span>{{ Consultant.cnsproject }}
                </div>
              </div>
              <div class="conpro-info-con2">
                <div><span> [ 약력 ] </span></div>
                <ul>
                  <li
                    v-for="(vo, descIndex) in Consultant.cnscareerVO"
                    :key="descIndex"
                  >
                    {{ vo.content }} ({{ vo.detail }})
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="Conpro-procon-false" v-else>
      <div class="Conpro-procon1-false">
        <h3 class="flase-move-text">
          이 서비스는 컨설턴트 매칭이후에 사용이 가능합니다.
        </h3>
        <button class="false-move-btn" @click="coninfomove">매칭하기</button>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      Consultant: null,
      memno: localStorage.getItem("memno"),
      conyn: false,
    };
  },
  methods: {
    btnmove() {
      this.$router.push({ name: "OneToOne" });
    },
    deslen(i) {
      if (this.Consultant.description[i].length > 20) {
        console.log("출력");
        return this.Consultant.description[i].substring(0, 20) + "...";
      } else {
        return this.Consultant.description[i];
      }
    },
    findcon() {
      const getmemno = new FormData();
      getmemno.append("memno", this.memno);
      axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/myconsult/mycon`, getmemno, {
          headers: { "Content-Type": "application/json" },
        })
        .then((res) => {
          if (res.data.email) {
            this.Consultant = res.data;
            console.log(this.Consultant);
            this.conyn = true;
          }
        });
    },
    coninfomove() {
      this.$router.push("ConsultantInfo");
    },
  },
  mounted() {
    this.findcon();
  },
};
</script>
<style>
.OTO-move-btn {
  background-color: #0a6065;
  height: 48px;
  width: 110px;
  border: #0f888f;
  border-radius: 4px;
  margin: 3% 0px;
  color: white;
  font-weight: 600;
  font-size: 18px;
  align-content: center;
  transition:
    background-color 0.5s,
    color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}
.OTO-move-btn:hover {
  background-color: #094346;
}
.blockquote-conpro {
  display: block;
  background: #fff;
  padding: 20px 20px 10px 45px;
  position: relative;

  /*Font*/
  font-size: 1.7em;
  line-height: 1.5;
  color: #11045e;

  /*Box Shadow - (Optional)*/
  -moz-box-shadow: 2px 2px 15px #ccc;
  -webkit-box-shadow: 2px 2px 15px #ccc;
  box-shadow: 2px 2px 15px #ccc;

  /*Borders - (Optional)*/
  border-left-style: solid;
  border-left-width: 15px;
  /* border-right-style: solid; */
  border-right-width: 0px;

  text-align: left;
}
.Conpro-con-1 {
  width: 90%;
  /* background: #0c1b49; */
  border-radius: 5px;
  color: white;
  margin-bottom: 2.5%;
  /* padding: 10px; */
}
.Conpro-procon {
  background: rgba(223, 223, 223, 0.3);
  width: 90%;
  border-radius: 5px;
  height: auto;
  padding: 3% 0%;
  box-shadow: 2px 2px 15px #ccc;
}
.Conpro-procon1 {
  display: flex;
  justify-content: space-between; /* 필요한 경우 요소 사이에 공간을 둠 */
  align-items: flex-start; /* 요소의 상단을 정렬 */
  gap: 20px; /* 요소 사이의 간격 */
}

.Conpro-procon-false {
  background: linear-gradient(
    45deg,
    rgba(0, 0, 0, 0.8),
    rgba(0, 0, 0, 0.5),
    rgba(12, 12, 12, 0.7)
  );
  width: 90%;
  border-radius: 5px;
  min-height: 400px;
  padding: 3% 0%;
  box-shadow: 2px 2px 15px #ccc;
}
.false-move-btn {
  width: 130px;
  padding: 10px 25px 10px;

  border: none;
  border-radius: 5px;

  font-weight: bold;
  font-size: 1.1rem;
  background-color: #102669;
  color: #fff;
  transition:
    background-color 0.5s,
    color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
}
/* 호버 상태에서 배경색 변경 */
.false-move-btn:hover {
  background-color: #f9f8ff;
  color: #102669;
}
.flase-move-text {
  margin-top: 100px;
  margin-bottom: 20px;
  color: white;
}
.Conimg img {
  border-radius: 5px;
  width: 300px;
  height: 400px;
}
.Conimg {
  width: 300px;
  height: 400px;
  margin-left: 20px;
  border-radius: 5px;
  margin-bottom: 15px;
  position: relative;
  display: flex;
  /* align-items: center; */
  justify-content: center;
  background-color: #000000;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 20px;
  overflow: hidden;
}

.Con-profile-section {
  background-color: white;
  padding: 25px;

  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  /* width: 810px; */
  height: 400px;
  width: 65%;
  margin-right: 30px;
}

.project-description {
  padding-right: 20px;
}
.project-description p {
  padding-left: 0;
}
.project-title {
  /* margin-top: 8%; */
  margin-bottom: 25px;
  border-bottom: 4px solid #12044e;
}
.conpro-info {
  font-weight: 600;
  text-align: left;
  font-size: 1.1em;
  display: flex; /* Flexbox 적용 */
  justify-content: space-between; /* 자식 요소를 양 끝에 배치 */
  align-items: flex-start; /* 자식 요소의 상단을 정렬 */
  gap: 20px; /* 두 요소 사이의 간격 */
}

.conpro-info span {
  font-weight: 700;
  font-size: 1.3em;
  display: inline-block;
}

.conpro-info-con1 {
  flex: 0.8;
}
.conpro-info-con2 {
  flex: 1; /* 자식 요소들이 동일한 비율로 너비를 차지하도록 설정 */
}
.conpro-info-int span {
  margin-bottom: 11px;
  font-weight: 700;
}
.conpro-info-int {
  font-weight: 600;
}
</style>
