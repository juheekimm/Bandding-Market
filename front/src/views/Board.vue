<template>
  <v-container class="Nanum">
    <v-layout wrap class="ma-0">
      <!-- 남은 시간 -->
      <v-flex
        sm12
        xs12
        class="my-1"
        v-bind:style="{
          background: timerColor(),
          'border-radius': '10px',
          'text-align': 'center',
          color: 'white',
          'font-size': '1.4rem',
        }"
      >
        <div
          v-if="
            remainTime.day == 0 &&
              remainTime.hours == 0 &&
              remainTime.minutes == 0 &&
              remainTime.seconds == 0
          "
        >
          <v-icon color="white" style="font-size: 2rem;">mdi-timer</v-icon>
          <span class="mx-1">공구가 마감됬습니다 </span>
        </div>
        <div v-else class="fw700">
          <v-icon color="white" style="font-size: 2rem;">mdi-timer</v-icon>
          <span class="mx-1">{{ remainTime.day }}일 </span>
          <span class="mx-1">{{ remainTime.hours }}시 </span>
          <span class="mx-1">{{ remainTime.minutes }}분 </span>
          <span class="mx-1">{{ remainTime.seconds }}초 </span>
          <!-- <span class="d-none d-sm-flex">남았습니다</span> -->
        </div>
      </v-flex>


      <!--태그-->
      <v-flex sm12 xs12>
        <span>
          <u>#{{ board.category == 0 ? '배달' : '상품' }}</u>
        </span>
      </v-flex>

      <!--제목-->
      <v-flex sm10 xs12 class="fw700">
        <!-- 크기가 큰 화면 -->
        <span class="titleSize d-none d-sm-flex">{{ board.title }}</span>
        <!-- xs -->
        <span class="titleMobileSize d-flex d-sm-none">{{ board.title }}</span>
      </v-flex>

      <!-- 작성자와 작성일 수정 삭제-->
      <v-flex sm12 xs12 style="color: gray;">
        <v-menu offset-y>
          <template v-slot:activator="{ on }">
            <span class="mr-3" v-on="on" style="cursor: pointer;"
              ><u>{{ board.user.nickname }}</u></span
            >
          </template>
          <v-list>
            <v-list-item
              @click="
                clickUser = board.user
                userInfoDailogFlag = !userInfoDailogFlag
              "
            >
              <v-list-item-title
                ><v-icon color="primary">mdi-account</v-icon
                ><span>유저정보보기</span></v-list-item-title
              >
            </v-list-item>
            <v-list-item @click="clickReport(board.user)">
              <v-list-item-title
                ><v-icon style="color: red;">mdi-alarm-light</v-icon
                ><span>유저신고하기</span></v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
        <span class="mx-2"
          >{{ board.writeDate.substring(0, 10) }}
          {{ board.writeDate.substring(11, 19) }}</span
        >
        <!-- <span class="mx-2"></span> -->
        <v-divider vertical></v-divider>
        <span>
          <v-chip
            v-for="(keyword, index) in board.keyword.split('#').slice(1)"
            :key="board.board_id + ' ' + index + ' ' + keyword"
            color="#f076b6"
            class="mx-1"
            style="color:white"
            small
          >
            #{{ keyword }}
          </v-chip>
        </span>
      </v-flex>

      <!-- 선 -->
      <v-flex sm12 xs12>
        <v-divider class="my-3"></v-divider>
      </v-flex>

      <!-- content -->
      <v-flex sm8 xs12>
        {{ board.context }}
      </v-flex>

      <!-- map -->
      <v-flex sm4 xs12>
        <div id="map" style="height: 300px; width: 100%;" class="my-2"></div>
      </v-flex>

      <!-- 선 -->
      <v-flex sm12 xs12>
        <v-divider class="my-3"></v-divider>
      </v-flex>

      <!-- 인원 sm화면 -->
      <v-flex sm12 class="outerFlex d-none d-sm-flex">
        <v-container>
          <v-row>
            <v-col>
              <div style="font-size: 2rem; text-align: center;" class="fw800">
                참여하기
                <v-btn class="mx-2 " fab dark large color="pink" @click="joinBoard">
                  <v-icon dark>mdi-heart</v-icon>
                </v-btn>
                <v-btn class="mx-2" fab dark large color="grey" @click="outBoard">
                  <v-icon dark>mdi-heart</v-icon>
                </v-btn>
                취소하기
              </div>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <div style="font-size: 1rem; text-align: center;">
                <span>
                  참여인원
                </span>
                <span style="font-size: 2rem;"
                  >{{ board.participants }} / {{ board.limit_num }}</span
                >
                <span>
                  제한인원
                </span>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </v-flex>

      <!-- 인원 xs화면 -->
      <v-flex xs12 class="outerFlex d-flex d-sm-none">
        <v-container class="ma-0">
          <v-row>
            <v-col>
              <div style=" text-align: center;" class="fw800">
                <v-btn class="mx-2 " fab dark large color="pink" @click="joinBoard">
                  <v-icon dark>mdi-heart</v-icon>참여
                </v-btn>
                <v-btn class="mx-2" fab dark large color="grey" @click="outBoard">
                  <v-icon dark>mdi-heart</v-icon>취소
                </v-btn>
              </div>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <div style="font-size: 1rem; text-align: center;">
                <span>
                  참여인원
                </span>
                <span style="font-size: 2rem;"
                  >{{ board.participants }} / {{ board.limit_num }}</span
                >
                <span>
                  제한인원
                </span>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </v-flex>

      <!-- 선 -->
      <v-flex sm12 xs12>
        <v-divider class="my-3"></v-divider>
      </v-flex>

      <!-- 댓글제목-->
      <v-flex sm12 xs12>
        <span>전체댓글</span>
        <span style="color: orange;"> {{ commentList.length }}</span>
        <span>개</span>
        <v-divider class="my-2"></v-divider>
      </v-flex>

      <v-flex sm11 xs12>
        <v-textarea
          clearable
          outlined
          clear-icon="mdi-cancel"
          label="댓글 입력"
          v-model="contents"
          hide-details
        ></v-textarea>
      </v-flex>

      <v-flex sm1 xs12 style="text-align: right;">
        <v-btn
          @click="writeComment"
          style="height: 100%;width: 100%;background: #82b1ff; color: white;"
          class="fw700"
          >댓글입력</v-btn
        >
      </v-flex>
    </v-layout>

    <v-flex sm12 xs12>
      <v-divider class="my-2"></v-divider>
    </v-flex>

    <!-- 댓글리스트 -->
    <v-hover
      v-slot:default="{ hover }"
      v-for="(comment, index) in commentList"
      :key="'commentapp' + index"
    >
      <v-container
        class="pa-0"
        v-bind:style="{
          width: '100%',
          background: hover ? '#e9e9e9' : 'white',
        }"
      >
        <v-row class="py-1">
          <v-col class="py-1">
            <v-menu offset-y>
              <template v-slot:activator="{ on }">
                <span
                  class="mr-3"
                  v-on="on"
                  style="cursor: pointer; text-decoration: underline; font-weight: bolder;"
                  >{{ comment.user.nickname }}</span
                >
              </template>
              <v-list>
                <v-list-item
                  @click="
                    clickUser = comment.user
                    userInfoDailogFlag = !userInfoDailogFlag
                  "
                >
                  <v-list-item-title
                    ><v-icon color="primary">mdi-account</v-icon
                    ><span>유저정보보기</span></v-list-item-title
                  >
                </v-list-item>
                <v-list-item @click="clickReport(comment.user)">
                  <v-list-item-title
                    ><v-icon style="color: red;">mdi-alarm-light</v-icon
                    ><span>유저신고하기</span></v-list-item-title
                  >
                </v-list-item>
              </v-list>
            </v-menu>
          </v-col>
          <v-spacer></v-spacer>
          <v-col class="py-1" style="text-align: right;">
            <span
              style="cursor: pointer; text-decoration: underline;"
              v-on:click="deleteComment(comment.comment_id)"
              class="mx-1"
              >수정</span
            >
            <span
              style="cursor: pointer; text-decoration: underline;"
              @click="deleteComment(comment.comment_id)"
              class="mx-1"
              >삭제</span
            >
          </v-col>
        </v-row>
        <v-row>
          <v-col> {{ comment.context }} </v-col>
        </v-row>
        <v-row style="background: white;">
          <v-col> <v-divider> </v-divider></v-col>
        </v-row>
      </v-container>
    </v-hover>

    <v-container>
      <v-row>
        <v-col cols="12" style="text-align:end">
          <v-btn @click="deleteBoard()">삭제</v-btn>
          <v-btn @click="goToSearch">
            목록으로 가기
          </v-btn>
        </v-col>
      </v-row>
    </v-container>

    <UserInfoDailog
      v-if="userInfoDailogFlag"
      :user="clickUser"
      v-on:updateUserDialogFlag="updateUserDialogFlag"
    ></UserInfoDailog>

    <v-dialog
      v-model="reportDailogFlag"
      max-width="290"
      class="noShadow"
      persistent
    >
      <v-card>
        <v-card-title style="font-size:2rem">
          <span> 회원정보보기 </span>
        </v-card-title>
        <v-divider></v-divider>
        <v-container>
          <v-row>
            <v-col class="pt-0">
              <b>신고 분류</b>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" class="py-0">
              <v-radio-group v-model="category" column hide-details>
                <v-row>
                  <v-col cols="6" class="py-1">
                    <v-radio label="욕설" value="욕설"></v-radio>
                  </v-col>
                  <v-col cols="6" class="py-1">
                    <v-radio label="광고" value="광고"></v-radio>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="6" class="py-1">
                    <v-radio label="미참석" value="미참석"></v-radio>
                  </v-col>
                  <v-col cols="6" class="py-1">
                    <v-radio label="기타" value="기타"></v-radio>
                  </v-col>
                </v-row>
              </v-radio-group>
            </v-col>
          </v-row>
          <v-divider></v-divider>

          <v-row>
            <v-col class="pt-3">
              <b>내용</b>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" class="py-0">
              <v-textarea
                solo
                label="신고내용을 입력해주세요."
                no-resize
                v-model="reportContent"
              ></v-textarea>
            </v-col>
          </v-row>
        </v-container>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="clickReportButton()" small>
            신고하기</v-btn
          >
          <v-btn
            color="blue darken-1"
            text
            @click="reportDailogFlag = false"
            small
          >
            취소</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import UserInfoDailog from '../components/UserInfoDialog'
import Constant from '../vuex/Constant'
import { mapState } from 'vuex'
import http from '../vuex/http-common'

export default {
  name: 'Board',
  components: {
    UserInfoDailog,
  },
  data: () => ({
    clickUser: {},
    remainTime: {},
    remainTimeFunction: null,
    map: null,
    headers: [
      { text: 'id', value: 'id', width: 10 },
      { text: 'context', value: 'context', width: 5 },
      { text: 'controller', value: 'controller', width: 10, align: 'right' },
    ],
    userInfoDailogFlag: false,
    contents: '',
    reportDailogFlag: false,
    reported: { nickname: '', user_id: '' },
    category: '욕설',
    reportContent: '',
  }),
  created() {
    if (!(window.kakao && window.kakao.maps && window.kakao.services))
      this.addMapScript()
    this.remainTimeFunction = setInterval(this.calRemainingTime, 1000)

    this.$store.dispatch(Constant.LOAD_COMMENTLIST, {
      bid: this.$route.query.id,
    })

    this.$store.dispatch(Constant.SEARCH_BOARD, {
      id: this.$route.query.id,
      callback: this.drawMap,
    })

    this.$vuetify.goTo(0)
  },
  mounted() {},
  computed: {
    ...mapState(['board', 'commentList']),
  },
  beforeDestroy() {
    clearInterval(this.remainTimeFunction)
  },
  methods: {
    test() {
      console.log('ddd')
    },
    test2(aa) {
      alert('삭제?' + aa)
    },
    addMapScript() {
      const script = document.createElement('script') /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        'http://dapi.kakao.com/v2/maps/sdk.js?appkey=053dd3145f395e73cbb5211bedf3e97f&libraries=services'
      document.head.appendChild(script)
    },
    drawMap() {
      console.log('draws')
      var mapContainer = document.getElementById('map') // 지도를 표시할 div
      var position = new kakao.maps.LatLng(
        this.board.board_locationY,
        this.board.board_locationX
      )
      var mapOption = {
        center: position, // 지도의 중심좌표
        level: 4, // 지도의 확대 레벨
      }

      this.map = new kakao.maps.Map(mapContainer, mapOption) // 지도를 생성합니다

      new kakao.maps.Marker({
        map: this.map,
        position: position,
      })
    },
    calRemainingTime() {
      let today = new Date()

      //현재시간과 마감날짜 계산
      var diff = Math.floor(
        (Date.parse(this.board.deadlineDate) - Date.parse(today)) / 1000
      )
      //마감시간이면 멈춘다.
      if (diff <= 0) {
        this.remainTime = {
          day: 0,
          hours: 0,
          minutes: 0,
          seconds: 0,
        }

        clearInterval(this.remainTimeFunction)
        return
      }

      var day = Math.floor(diff / (3600 * 24))
      var hours = Math.floor((diff - day * 3600 * 24) / 3600)
      var minutes = Math.floor((diff - day * 3600 * 24 - hours * 3600) / 60)
      var seconds = diff - day * 3600 * 24 - hours * 3600 - minutes * 60

      // console.log(day + ' ' + hours + ' ' + minutes + ' ' + seconds)
      // console.log(
      //   this.remainTime.day +
      //     ' ' +
      //     this.remainTime.hours +
      //     ' ' +
      //     this.remainTime.minutes +
      //     ' ' +
      //     this.remainTime.seconds
      // )

      this.remainTime = {
        day: day,
        hours: hours,
        minutes: minutes,
        seconds: seconds,
      }
      // }
    },
    timerColor() {
      if (
        this.remainTime.day == 0 &&
        this.remainTime.hours == 0 &&
        this.remainTime.minutes == 0 &&
        this.remainTime.seconds == 0
      ) {
        return '#ffa5a5'
      } else {
        return '#39dfed'
      }
    },
    updateUserDialogFlag(flag) {
      this.userInfoDailogFlag = flag
    },
    writeComment() {
      http
        .post('/board/comment', {
          board: {
            board_id: this.$route.query.id,
          },
          context: this.contents,
          user: {
            user_id: this.$cookies.get('user_id'),
          },
        })
        .then((res) => {
          console.log(res)
          window.location.reload()
        })
        .catch((err) => {
          alert('서버가 불안정합니다.')
          console.log(err)
        })
    },
    joinBoard() {
      console.log(this.$cookies.get('user_id'))
      if (
        this.$cookies.get('token') == '' ||
        this.$cookies.get('user_id') == ''
      ) {
        alert('로그인 후 이용해주세요')
        return
      }
      //  0 - 참가성공, 1 - 마감, 2 - 중복된유저 신청, 3 - 제한인원 초과
      http
        .get(
          '/board/' + this.$route.query.id + '/' + this.$cookies.get('user_id')
        )
        .then((res) => {
          // console.log(res.data.object)
          var signal = res.data.object
          if (signal == 0) {
            alert('참가신청완료')
            window.location.reload()
          } else if (signal == 1) {
            alert('마감된 게시물입니다.')
          } else if (signal == 2) {
            alert('이미 신청한 게시물입니다.')
          } else {
            alert('제한인원 초과')
          }
        })
        .catch((err) => {
          alert('서버와의 연결이 불안정합니다.')
          console.log(err)
        })
    },
    outBoard() {
      console.log(this.$cookies.get('user_id'))
      if (
        this.$cookies.get('token') == '' ||
        this.$cookies.get('user_id') == ''
      ) {
        alert('로그인 후 이용해주세요')
        return
      }
      //  0 - 취소 성공, 1 - 마감, 2 - 없는 유저 취소, 3 - 호스트가 취소
      http
        .delete(
          '/board/' + this.$route.query.id + '/' + this.$cookies.get('user_id')
        )
        .then((res) => {
          // console.log(res.data.object)
          var signal = res.data.object
          console.log(signal)
          if (signal == 0) {
            alert('참여취소 성공했습니다.')
            window.location.reload()
          } else if (signal == 1) {
            alert('마감된 게시물입니다.')
          } else if (signal == 2) {
            alert('신청하지 않은 유저입니다.')
          } else {
            alert('호스트는 참가취소가 불가능합니다.')
          }
        })
        .catch((err) => {
          alert('서버와의 연결이 불안정합니다.')
          console.log(err)
        })
    },
    deleteComment(cid) {
      console.log(cid)
      http
        .delete('/board/comment/' + cid)
        .then((res) => {
          alert('삭제가 완료되었습니다..')
          window.location.reload()
          res
        })
        .catch((err) => {
          alert('서버가 불안정합니다.')
          console.log(err)
        })
    },
    goToSearch() {
      this.$router.push('/search')
    },
    updateBoard() {
      console.log(this.board)
      this.$router.push({
        name: 'BoardUpdate',
        params: {
          bid: this.$router.query.id,
          title: this.board.title,
          category: this.board.category == 1 ? '음식' : '상품',
        },
      })
    },
    deleteBoard() {
      http
        .delete('/board/' + this.$route.query.id)
        .then((res) => {
          alert('삭제가 완료되었습니다..')
          this.$router.push({ path: '/search' })
          res
        })
        .catch((err) => {
          alert('서버가 불안정합니다.')
          console.log(err)
        })
    },
    clickReport(reported) {
      if (
        this.$cookies.get('token') == null ||
        this.$cookies.get('user_id') == null
      ) {
        alert('로그인부터 해주세요.')
        return
      }

      this.reported = reported
      this.reportDailogFlag = true
    },
    clickReportButton() {
      if (
        this.$cookies.get('token') == null ||
        this.$cookies.get('user_id') == null
      ) {
        alert('로그인부터 해주세요.')
        return
      }

      console.log(this.category)
      console.log(this.reported)
      console.log(this.reportContent)

      http
      .post('/report/insert', {
        category: this.category,
        context: this.reportContent,
        reported: {
          user_id: this.reported.user_id,
        },
        writer: {
          user_id: this.$cookies.get('user_id'),
        },
      })
      .then((res) => {
        console.log(res)
        alert("신고가 접수되었습니다.")
        this.reportDailogFlag = false
      })
      .catch((err) => {
        err
        alert("서버가 불안정 합니다.")
      })
    },
  },
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800 &display=swap');

.Nanum {
  font-family: 'Nanum Gothic', sans-serif;
}

.fw700 {
  font-weight: 700;
}

.fw800 {
  font-weight: 800;
}

.titleSize {
  font-size: 3rem;
}
.titleMobileSize {
  font-size: 1.4rem;
}
.center {
  align-self: center;
  text-align: center;
}
.outerFlex {
  background: #dffbff;
  border-radius: 20px;
  padding: 10px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.insertCommentButton {
  height: 100%;
  width: 100%;
  background: orange;
  color: white;
}

.v-input--selection-controls {
  margin-top: 0px;
  padding-top: 0px;
}
</style>
