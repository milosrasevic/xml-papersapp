<template>
  <v-content>
    <v-container>
      <v-row v-if="isLoggedIn">
        <v-col cols="12">
          <v-card v-if="!bodyTypeDetermined" class="ma-3 pa-6" @click="goTo('/body-type')">
            <v-card-text class="text-center">
              <h1>Find your body type</h1>
            </v-card-text>
          </v-card>
          <v-card v-if="bodyTypeDetermined" class="ma-3 pa-6" @click="myPlan()">
            <v-card-text class="text-center">
              <h1>My nutrition plan</h1>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-content>
</template>

<script>
import store from "@/store";
import UserService from "../api-services/user.service";

export default {
  name: "Home",
  data: () => ({
    user: {},
    bodyTypeDetermined: false
  }),
  computed: {
    isLoggedIn() {
      if (store.state.userLoggedIn) {
        return true;
      } else {
        return false;
      }
    }
  },
  beforeMount() {
    this.getUserPlan();
  },
  methods: {
    goTo(url) {
      this.$router.push(url);
    },
    myPlan() {
      this.$router.push("myPlan/" + store.state.userId);
      // this.$router.push('myPlan/');
    },
    getUserPlan() {
      UserService.getUser(store.state.userId).then(response => {
        this.user = response.data;
        if (this.user.bodyType == null) {
          this.bodyTypeDetermined = false;
        } else {
          this.bodyTypeDetermined = true;
        }
        console.log(this.user);
      });
    }
  }
};
</script>
