<template>
  <div class="container">
    <div class="container2">
      <div class="CTtitle">
        <blockquote class="blockquote-CT">
          <b>내가꿈과 함께하는 전문 컨설턴트</b>
        </blockquote>
      </div>
    </div>

    <div class="section secondary-section" id="portfolio">
      <div class="triangle"></div>
      <div class="info-container">
        <div
          v-for="(project, index) in filteredProjects"
          :key="index"
          class="toggleDiv row-fluid single-project"
          :id="'slidingDiv' + index"
          v-show="activeProjectIndex === index"
        >
          <div class="span consultantImage">
            <img
              :src="imgpath(project.imgname)"
              :alt="'project ' + (index + 1)"
            />
          </div>
          <div class="span6">
            <div class="project-description">
              <div class="project-title clearfix">
                <h2>{{ project.category }}&nbsp;{{ project.name }}</h2>
                <span class="show_hide close" @click="hideProject"> X </span>
              </div>
              <div class="project-info">
                <div><span>Name : &nbsp;</span>{{ project.name }}</div>
                <div>
                  <span>introduce : &nbsp;</span>{{ project.introduce }}
                </div>
                <div>
                  <span>Email : &nbsp;</span
                  ><a :href="project.email">{{ project.email }}</a>
                </div>
                <div><span> [ 약력 ] </span></div>
                <ul>
                  <li
                    class="project-profile"
                    :v-if="project.cnscareerVO"
                    v-for="(desc, descIndex) in project.cnscareerVO"
                    :key="descIndex"
                  >
                    [{{ desc.careerdiv }}] &nbsp; {{ desc.term }} &nbsp;
                    {{ desc.content }} &nbsp; {{ desc.detail }}
                  </li>
                </ul>
                <button
                  v-if="this.use.CNT == 0"
                  class="application-btn"
                  id=""
                  type="button"
                  @click="application(project.cnsno)"
                >
                  신청
                </button>
                <button
                  v-if="this.use.CNSNO == project.cnsno"
                  class="application-btn"
                  id=""
                  type="button"
                  @click="cancle"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
        <ul class="nav nav-pills">
          <li
            class="filter"
            :class="{ active: currentFilter === 'all' }"
            @click="setFilter('all')"
          >
            <a href="#noAction">All</a>
          </li>
          <li
            class="filter"
            :class="{ active: currentFilter === 'IT/개발' }"
            @click="setFilter('IT/개발')"
          >
            <a href="#noAction">It/개발</a>
          </li>
          <li
            class="filter"
            :class="{ active: currentFilter === '교육' }"
            @click="setFilter('교육')"
          >
            <a href="#noAction">교육</a>
          </li>
          <li
            class="filter"
            :class="{ active: currentFilter === '영업/마케팅' }"
            @click="setFilter('영업/마케팅')"
          >
            <a href="#noAction">영업/마케팅</a>
          </li>
          <li
            class="filter"
            :class="{ active: currentFilter === '기획/전략' }"
            @click="setFilter('기획/전략')"
          >
            <a href="#noAction">기획/전략</a>
          </li>
          <li
            class="filter"
            :class="{ active: currentFilter === '경영' }"
            @click="setFilter('경영')"
          >
            <a href="#noAction">경영</a>
          </li>
        </ul>
        <ul id="portfolio-grid" class="thumbnails row">
          <li
            v-for="(project, index) in filteredProjects"
            :key="index"
            :class="['span4', 'mix', project.category]"
          >
            <div class="thumbnail">
              <img
                :src="`http://localhost:81/yourdream/uploads/${project.imgname}`"
                :alt="'project ' + (index + 1)"
              />
              <div class="mask">
                <button class="more show_hide" @click="showProject(index)">
                  + more..
                </button>
              </div>
              <div class="caption">
                <h3 style="font-size: 20px">
                  [{{ project.category }}] {{ project.name }}
                </h3>
                <p style="font-size: 15px" v-if="project.introduce">
                  {{ truncateText(project.introduce, 60) }}
                </p>
                <ul class="profile-list" style="font-size: 20px">
                  -{{
                    project.name
                  }}
                  약력 -
                  <li
                    v-for="(desc, descIndex) in project.cnscareerVO"
                    :key="descIndex"
                    style="font-size: 15px"
                  >
                    {{ desc.content }}
                  </li>
                  <li v-if="project.introduce && project.introduce.length > 3">
                    ...
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import AOS from "aos";
import "aos/dist/aos.css";
import axios from "axios";
export default {
  created() {
    AOS.init();
    this.fetchData();
  },
  data() {
    return {
      memno: localStorage.getItem("memno"),
      use: [],
      currentFilter: "all",
      projects: [],
      activeProjectIndex: null,
    };
  },
  computed: {
    filteredProjects() {
      if (this.currentFilter === "all") {
        return this.projects;
      }
      return this.projects.filter(
        (project) => project.category === this.currentFilter
      );
    },
  },
  methods: {
    fetchData() {
      axios
        .post(
          `${process.env.VUE_APP_BACK_END_URL}/consultantInfo/consultantList`
        )
        .then(
          (res) => {
            this.projects = res.data;
            console.log(res.data);
            console.log("현재 회원:", this.memno);
          },
          axios
            .post(
              `${process.env.VUE_APP_BACK_END_URL}/consultantInfo/consultantUse`,
              { memno: this.memno }
            )
            .then((res) => {
              this.use = res.data;
              console.log(res.data);
              this.activeProjectIndex = res.data.CNSNO - 1;
              console.log(this.activeProjectIndex);
            })
        );
    },
    setFilter(filter) {
      this.currentFilter = filter;
      this.hideProject();
    },
    showProject(index) {
      this.activeProjectIndex = index;
      this.$nextTick(() => {
        const target = document.querySelector(`#slidingDiv${index}`);
        if (target) {
          target.scrollIntoView({ behavior: "smooth" });
        }
      });
    },
    hideProject() {
      this.activeProjectIndex = null;
    },
    truncateText(text, maxLength) {
      if (text) {
        return text.length > maxLength
          ? text.substring(0, maxLength) + "..."
          : text;
      } else {
        return "...";
      }
    },
    application(cnsno) {
      axios
        .post(
          `${process.env.VUE_APP_BACK_END_URL}/consultantInfo/consultantAppl`,
          { memno: this.memno, cnsno: cnsno },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(() => {
          console.log("면접자 :", this.memno, "=>", "컨설턴트", cnsno);
          alert(
            "컨설턴트가 배정되었습니다. 이제 AI 모의면접을 보면 컨설턴트의 코칭을 받을 수 있습니다."
          );
          window.location.reload();
        });
    },
    cancle() {
      axios
        .post(
          `${process.env.VUE_APP_BACK_END_URL}/consultantInfo/consultantCancle`,
          { memno: this.memno }
        )
        .then(() => {
          console.log("취소 완료");
          alert(
            "컨설턴트가 취소되었습니다. 이제 다른 컨설턴트를 신청 할 수 있습니다."
          );
          window.location.reload();
        });
    },
    imgpath(imgname) {
      // return `localhost:3001/public/img/upimg/${project.imgname}`;
      return `http://localhost:81/yourdream/uploads/${imgname}`;
    },
  },
};
</script>
