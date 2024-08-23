<template>
  <div class="container">
    <div class="cv-container">
      <div class="CVtitle">
        <blockquote class="blockquote-CV">
          <b> <p>이력서관리</p></b>
        </blockquote>
      </div>
      <!-- <div class="cv-subtitle">내 이력서</div> -->
      <div class="cv-notice">
        <p>
          📢&nbsp; 이력서 사진을 찍어 올려보세요. 인식 후 오타가 없는지 꼭
          확인하세요.
        </p>
      </div>

      <div class="cv-listform" v-if="show">
        <div class="cv-list">
          <table class="cv-table">
            <tr class="cv-tr1">
              <th>이력서 제목</th>
              <th>관리</th>
              <th>수정 날짜</th>
            </tr>
            <!-- 이쪽 데이터받으면 for 문-->
            <tr
              v-for="(item, index) in items"
              :key="index"
              class="cv-tr2"
              :v-if="show"
            >
              <td>
                <div class="cv-cvtitle" @click="updatecv(item.rsmno)">
                  {{ item.title }}
                </div>
              </td>
              <td class="cvbtn-area">
                <button class="cv-upbtn" @click="updatecv(item.rsmno)">
                  수정
                </button>
                <button class="cv-delbtn" @click="deletecv(item.rsmno)">
                  삭제
                </button>
              </td>
              <td>
                <div class="cv-update-date">{{ dateFormat(item.upddt) }}</div>
              </td>
            </tr>
            <!--  -->
          </table>
        </div>
      </div>
      <div class="cv-listform-nolist" v-if="!show">
        <img class="cv-nolist-img" src="\img\cv_nolist.png" />
        <div class="cv-nolist-text">작성된 이력서가 없습니다.</div>
      </div>

      <div class="cv-dwrite">
        <router-link to="resumeWrite"
          ><button class="cv-writebtn">이력서 작성</button></router-link
        >
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      show: true,
      items: {},
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/resume/resumeList`, {
          memno: localStorage.getItem("memno"),
        })
        .then((res) => {
          console.log("호출 성공");
          this.items = res.data;
          console.log("memno:", res.data);
          console.log(this.items);
          if (res.data.length === 0) {
            this.show = false;
          } else {
            this.show = true;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    dateFormat(str) {
      return str.split("T")[0];
    },
    updatecv(num) {
      this.$router.push({ name: "ResumeUpdate", query: { num: num } }); //나중에 파라미터 넣어주세요
    },
    deletecv(rsmno) {
      axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/resume/resumeDelete`, {
          rsmno: rsmno,
          seqno: null,
        })
        .then((res) => {
          console.log("삭제완료");
          this.fetchData();
        });
    },
  },
};
</script>
