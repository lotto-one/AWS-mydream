<template>
  <div class="container">
    <div class="interRestitle">
      <blockquote class="blockquote-interRes">
        <b><p>면접결과 관리</p></b>
      </blockquote>
    </div>
    <hr />
    <div class="interviewRes-tab row">
      <div
        @click="pagechage(1)"
        class="col-1"
        :class="{
          'interviewRes-lbutton': true,
          interviewactive: activePage === 1,
        }"
      >
        인성면접결과
      </div>
      <div
        @click="pagechage(2)"
        class="col-1"
        :class="{
          'interviewRes-button': true,
          interviewactive: activePage === 2,
        }"
      >
        직무면접결과
      </div>
    </div>
    <div class="interviewRes-con">
      <!-- 인성면접결과 -->
      <div
        :class="{
          'display-block': content === 1,
          'display-none': content !== 1,
        }"
        class="row scrollable-div interviewRes-display"
      >
        <div
          v-for="(e, index) in sortedAttitude"
          :key="index"
          class="interviewRes-displaytwo"
        >
          <table v-show="e !== null" class="interviewRes-table">
            <tr>
              <td
                class="interviewRes-list"
                @click="showModal_personal(e.INTNO)"
              >
                <li style="padding: 15px;">
                  <b>[인성면접]</b> {{ e.MNAME }}님 면접결과 
                  <span style="font-size: 0.75em; color: #949494;">({{ formatDate(e.CREDT) }})</span>
                </li>
              </td>
            </tr>
          </table>
        </div>
        <div v-show="sortedAttitude.length === 0" class="interviewRes-empty">
          <div class="interviewRes-empty-content">
            <img
              src="img/InterviewRes_image/nopersonal.png"
              class="interviewRes-empty-image"
            />
            <div class="interviewRes-empty-button">
              <router-link to="AISetting">
                <button class="btn btn-next interviewRes-empty-btn">
                  AI 모의면접 보러 가기
                </button>
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 직무면접결과 -->
      <div
        :class="{
          'display-block': content === 2,
          'display-none': content !== 2,
        }"
        class="row scrollable-div interviewRes-display"
      >
        <div
          v-for="(e, index) in sortedCareer"
          :key="index"
          class="interviewRes-displaytwo"
        >
          <table v-show="e !== null" class="interviewRes-table">
            <tr>
              <td class="interviewRes-list" @click="showModal_duty(e.INTNO)">
                <li style="padding: 15px;">
                  <b>[직무면접]</b> {{ e.MNAME }}님 면접결과 
                  <span style="font-size: 0.75em; color: #949494;">({{ formatDate(e.CREDT) }})</span>
                </li>
              </td>
            </tr>
          </table>
        </div>
        <div v-show="sortedCareer.length === 0" class="interviewRes-empty">
          <div class="interviewRes-empty-content">
            <img
              src="img/InterviewRes_image/noduty.png"
              class="interviewRes-empty-image"
            />
            <div class="interviewRes-empty-button">
              <router-link to="AISetting">
                <button class="btn btn-next interviewRes-empty-btn">
                  AI 모의면접 보러 가기
                </button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <InterviewResPersenalModal
    v-if="popState_personal"
    @close="closeModal_personal"
    v-bind:intno="intno"
  ></InterviewResPersenalModal>
  <InterviewResDutyModal
    v-if="popState_duty"
    @close="closeModal_duty"
    v-bind:intno="intno"
  ></InterviewResDutyModal>
  <OneToOne v-if="popState_one" @close="closeModal_one"/>
</template>

<script>
import axios from "axios";
import InterviewResPersenalModal from "../components/InterviewResModal_persenal.vue";
import InterviewResDutyModal from "../components/InterviewResModal_duty.vue";

export default {
  components: {
    InterviewResPersenalModal,
    InterviewResDutyModal,
  },
  data() {
    return {
      link: "/InterviewRes",
      activePage: 1,
      page: 1,
      attitude: [],
      career: [],
      content: 1,
      popState_personal: false,
      popState_duty: false,
      selectedIntno: null,
      intno: 5,
      memno: localStorage.getItem("memno"),
    };
  },
  mounted() {
    this.fetchAttitudeResults();
    this.fetchCareerResults();
  },
  computed: {
    sortedAttitude() {
      return this.attitude.slice().sort((a, b) => b.INTNO - a.INTNO);
    },
    sortedCareer() {
      return this.career.slice().sort((a, b) => b.INTNO - a.INTNO);
    },
  },
  methods: {
    pagechage(num) {
      this.activePage = num;
      this.page = num;
      this.content = num;
    },
    showModal_personal(intno) {
      this.popState_personal = true;
      this.intno = intno;
      console.log("intno넘어오나요?" + intno);
    },
    closeModal_personal() {
      this.popState_personal = false;
    },
    showModal_duty(intno) {
      this.popState_duty = true;
      this.intno = intno;
      console.log("intno넘어오나요?" + intno);
    },
    closeModal_duty() {
      this.popState_duty = false;
    },
    async fetchAttitudeResults() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/itv/attitude`,
          { params: { memno: this.memno } }
        );
        this.attitude = response.data;
        console.log("Fetched attitude results:", this.attitude);
      } catch (error) {
        console.error("Error fetching attitude results:", error);
      }
    },
    async fetchCareerResults() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/itv/career`,
          { params: { memno: this.memno } }
        );
        this.career = response.data;
        console.log("Fetched career results:", this.career);
      } catch (error) {
        console.error("Error fetching career results:", error);
      }
    },
    formatDate(datetime) {
      return datetime.split("T")[0];
    },
  },
};
</script>
