<template>
  <div class="container">
    <div>
      <div style="display: flex; justify-content: flex-start">
        <div class="box14">
          <FullCalendar v-if="!isLoading" :options="calendarOptions" />
        </div>

        <div class="box13-container">
          <div class="box13">
            <p class="box-text">D-Day</p>
            <hr class="box-line" />
            <ul class="cal-day">
              <li v-for="(dDay, index) in dDays" :key="index">
                <strong>{{ dDay.title }}</strong
                >: {{ dDay.daysLeft }}일 남음
              </li>
            </ul>
          </div>

          <div class="box13">
            <p class="box-text">일정</p>
            <hr class="box-line" />
            <ul class="cal-day">
              <li v-for="event in eventsList" :key="event.title">
                <strong>{{ event.title }}</strong
                >: {{ event.startdt }} ~ {{ event.enddt || event.startdt }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div>
      <!-- 모달 창 -->
      <CalendarModal
        v-if="isModalVisible"
        @close="closeModal"
        :modal-event="modalEvent"
        @update-event="updateModalEvent"
        @delete-event="deleteModalEvent"
      />
    </div>
  </div>
</template>

<script>
import { ref, defineComponent, onMounted, computed } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import CalendarModal from "../components/CalendarModal.vue";
import dayjs from "dayjs"; // 날짜 형식 변환을 위해 dayjs 사용
import axios from "axios";

const ScheduleVO = {
  schno: null,
  memno: localStorage.getItem("memno"),
  startdt: null,
  enddt: null,
  title: null,
  content: null,
  credt: null,
  upddt: null,
};

export default defineComponent({
  components: {
    FullCalendar,
    CalendarModal,
  },

  setup() {
    const schedule = ref(null);
    const isLoading = ref(true);
    const error = ref(null);
    const isModalVisible = ref(false);
    const modalEvent = ref({ id: null, title: "", startdt: "", enddt: "" });
    const lastYearMonth = ref("");

    const fetchSchedule = async (yearMonth = null) => {
      try {
        isLoading.value = true;

        const endOfMonth = dayjs(`${yearMonth}-01`)
          .endOf("month")
          .format("YYYY-MM-DD"); // 해당 년월의 마지막 날 계산

        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/schedule`,
          {
            params: {
              memno: localStorage.getItem("memno"),
              startdt: `${yearMonth}-01`, // 해당 월의 첫날
              enddt: endOfMonth, // 해당 월의 마지막 날
            },
          }
        );

        console.log("API 응답 데이터.events:", response.data.events);
        console.log("API 응답 데이터.ddays:", response.data.ddays);
        schedule.value = response.data;

        if (response.data && Array.isArray(response.data.events)) {
          // 캘린더 이벤트 데이터 설정
          const newEvents = response.data.events.map((e) => ({
            id: e.schno,
            title: e.title,
            start: e.startdt,
            end: e.enddt || e.startdt,
          }));

          // 이벤트만 업데이트
          calendarOptions.value.events = newEvents;
        } else {
          console.error("응답 데이터에 events 속성이 없습니다.");
          calendarOptions.value.events = [];
        }
      } catch (err) {
        console.error("일정정보를 가져오는 중 오류 발생:", err);
        error.value = "데이터를 불러오는 데 실패했습니다.";
      } finally {
        isLoading.value = false;
      }
    };

    onMounted(async () => {
      const currentYearMonth = dayjs().format("YYYY-MM"); // 페이지 로드 시 현재 년월을 가져옴
      await fetchSchedule(currentYearMonth);
    });

    const handleSelect = (info) => {
      const title = prompt("이벤트 제목을 입력하세요:");
      if (title) {
        addEvent(title, info.startStr, info.endStr);
      }
    };

    const addEvent = async (title, startdt, enddt) => {
      try {
        const newEvent = {
          title,
          startdt: startdt,

          enddt: enddt,
          memno: localStorage.getItem("memno"),
        };

        const response = await axios.post(
          `${process.env.VUE_APP_BACK_END_URL}/schedule/add`,
          newEvent,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.status === 200) {
          calendarOptions.value.events.push(newEvent);
          await fetchSchedule(lastYearMonth.value);
        } else {
          alert("이벤트를 저장하는 데 실패했습니다.");
        }
      } catch (error) {
        console.error("이벤트 저장 중 오류 발생:", error);
        alert("이벤트를 저장하는 도중 오류가 발생했습니다.");
      }
    };

    const updateEvent = async (event) => {
      modalEvent.value = {
        id: event.id,
        title: event.title,
        startdt: dayjs(event.start).format("YYYY-MM-DD"),
        enddt: event.end ? dayjs(event.end).format("YYYY-MM-DD") : dayjs(event.start).format("YYYY-MM-DD"),
      };
      isModalVisible.value = true;
    };

    const updateModalEvent = async (updatedEvent) => {
      if (!updatedEvent.schno && updatedEvent.id) {
        updatedEvent.schno = updatedEvent.id;
      }

      const finalEvent = {
        schno: updatedEvent.schno,
        title: updatedEvent.title,
        startdt: updatedEvent.startdt,
        enddt: updatedEvent.enddt || updatedEvent.startdt,
      };

      if (finalEvent && finalEvent.startdt && finalEvent.enddt) {
        try {
          const response = await axios.post(
            `${process.env.VUE_APP_BACK_END_URL}/schedule/update`,
            finalEvent,
            {
              headers: {
                "Content-Type": "application/json",
              },
            }
          );

          if (response.status === 200) {
            await fetchSchedule(lastYearMonth.value);
            isModalVisible.value = false;
          } else {
            alert("이벤트를 수정하는 데 실패했습니다.");
          }
        } catch (error) {
          alert("이벤트를 수정하는 도중 오류가 발생했습니다.");
        }
      } else {
        console.error("이벤트의 시작 날짜와 종료 날짜가 존재하지 않습니다.");
      }
    };

    const deleteModalEvent = async (event) => {
      if (confirm("이 이벤트를 삭제하시겠습니까?")) {
        try {
          const response = await axios.post(
            `${process.env.VUE_APP_BACK_END_URL}/schedule/delete`,
            { schno: event.id },
            {
              headers: {
                "Content-Type": "application/json",
              },
            }
          );

          if (response.status === 200) {
            await fetchSchedule(lastYearMonth.value);
            isModalVisible.value = false;
          } else {
            alert("이벤트를 삭제하는 데 실패했습니다.");
          }
        } catch (error) {
          alert("이벤트를 삭제하는 도중 오류가 발생했습니다.");
        }
      }
    };

    const closeModal = () => {
      isModalVisible.value = false;
    };

    const calendarOptions = ref({
      plugins: [dayGridPlugin, interactionPlugin],
      initialView: "dayGridMonth",
      events: [],
      selectable: true,
      select: handleSelect,
      eventClick: function (info) {
        updateEvent(info.event);
      },
      datesSet: function (info) {
        // const startDate = dayjs(info.start).add(1, 'month'); // 한 달을 더한 후
        // const currentYearMonth = startDate.format('YYYY-MM');  // 현재 년월 계산
        const currentYearMonth = dayjs(info.view.currentStart).format(
          "YYYY-MM"
        ); // 현재 월 계산

        console.log("현재 보이는 년월:", currentYearMonth);

        // 현재 보이는 년월이 이전에 요청한 년월과 다를 경우에만 요청 실행
        if (currentYearMonth !== lastYearMonth.value) {
          lastYearMonth.value = currentYearMonth;
          // fetchSchedule(currentYearMonth);
        }

      },
    });
    const dDays = computed(() => {
      if (!schedule.value || !Array.isArray(schedule.value.ddays)) return [];
      return schedule.value.ddays.map((dDay) => ({
        title: dDay.title,
        daysLeft: dDay.daysLeft,
      }));
    });

    const eventsList = computed(() => {
      if (!schedule.value || !Array.isArray(schedule.value.events)) return [];
      return schedule.value.events.map((event) => ({
        title: event.title,
        startdt: event.startdt,
        enddt: event.enddt || event.startdt,
      }));
    });

    return {
      schedule,
      isLoading,
      error,
      calendarOptions,
      handleSelect,
      closeModal,
      addEvent,
      updateEvent,
      isModalVisible,
      modalEvent,
      dDays,
      eventsList,
      updateModalEvent,
      deleteModalEvent,
    };
  },
});
</script>
