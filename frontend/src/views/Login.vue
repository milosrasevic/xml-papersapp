<template>
  <div id="logindiv">
    <v-content>
      <v-container id="loginform" fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-toolbar dark color="black">
                <v-toolbar-title>Log in</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form ref="form" v-model="isValid" lazy-validation>
                  <v-text-field
                    prepend-icon="mdi-account"
                    v-model="loginData.username"
                    :rules="username_rules"
                    name="login"
                    label="Username"
                    type="text"
                  ></v-text-field>
                  <v-text-field
                    prepend-icon="mdi-lock-question"
                    v-model="loginData.password"
                    :rules="password_rules"
                    name="password"
                    label="Password"
                    id="password"
                    type="password"
                  ></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn :disabled="!isValid" color="primary" @click="validateLogin">Login</v-btn>
                <v-btn color="white" @click="signIn">Sign up</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
    <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import LoginData from "@/model/LoginData.js";
import LoginService from "@/api-services/auth.service.js";
import store from "@/store";

export default {
  name: "Login",
  // store: store,
  data: () => ({
    isValid: true,
    username_rules: [v => !!v || "Username field can't be empty"],
    password_rules: [v => !!v || "Password field can't be empty"],
    loginData: new LoginData(),
    snackbar: {
      show: false,
      color: "",
      msg: ""
    }
  }),
  methods: {
    validateLogin() {
      if (this.$refs.form.validate()) {
        this.onSubmit();
      }
    },
    onSubmit() {
      LoginService.login(this.loginData)
        .then(response => {
          console.log(store.state.userLoggedIn);
          console.log(response.data.userWithAuthoritiesDTO.id);
          console.log(response.data);
          this.showSnackbar("Successful login!", "success");
          if(response.data.userWithAuthoritiesDTO.authorities[0].authority == "ROLE_ADMIN") {
            this.$router.push("/adminPage")
          } else {
            this.$router.push("/");
          }
        })
        .catch(() => {
          this.showSnackbar("Bad credentials", "error");
        });
    },
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
    },
    signIn() {
      this.$router.push("signIn");
    }
  }
};
</script>

<style>
#logindiv {
  height: 100%;
  /* background-color: #99bffc; */
}

#loginform {
  padding-top: 5%;
}
</style>
