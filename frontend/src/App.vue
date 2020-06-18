<template>
  <div id="app">
    <span class="bg"></span>
    <v-app>
      <v-app-bar color="black" dense dark short app fixed>
        <v-btn @click="goTo('/')" >
          <v-icon>mdi-home</v-icon>
        </v-btn>
         <v-btn @click="goTo('/profile')" v-if="isAuthor">
          <v-icon>mdi-face</v-icon>
        </v-btn>
        <v-divider class="mx-4" vertical ></v-divider>
          <v-spacer></v-spacer>
          <v-divider class="mx-4" vertical ></v-divider>
          <v-btn v-if="isLoggedIn == false" @click="goTo('/signup')">Sign up</v-btn>
          <v-btn v-if="isLoggedIn == false" @click="goTo('/login')">Login</v-btn>
          <v-btn v-if="isLoggedIn == true" @click="goTo('/logout')">Logout</v-btn>
      </v-app-bar>
      <v-content>
        <router-view />
      </v-content>
    </v-app>
  </div>
</template>

<script>
import store from '@/store';

export default {
  name: "App",
  data: () => ({
  }),
  computed: {
    isLoggedIn() {
      console.log("logged in")
      console.log(store.state.userLoggedIn)
      return store.state.userLoggedIn;
    },
    isAuthor() {
      return this.$store.getters.isAuthor;
    }
    // isUser() {
    //   return store.state.user.authorities ? store.state.user.authorities[0].authority == "ROLE_REGISTERED" : true;
    // }
  },
  components: {},
  
  methods: {
    goTo(url) {
      this.$router.push(url);
    }
  }
};
</script>
