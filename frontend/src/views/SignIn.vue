<template>
  <div style="height: 100%;">
    <v-content>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-form ref="form" v-model="formValid" lazy-validation>
                <v-toolbar dark color="black">
                  <v-toolbar-title>Create a new account</v-toolbar-title>
                  <v-spacer></v-spacer>
                </v-toolbar>
                <v-card-text>
                  <v-layout wrap>
                    <v-flex xs12 sm6>
                      <v-text-field
                        label="First name*"
                        v-model="registration.firstName"
                        :rules="firstname_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      <v-text-field
                        label="Last name*"
                        v-model="registration.lastName"
                        :rules="lastname_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        label="Username*"
                        v-model="registration.username"
                        :rules="username_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      <v-text-field
                        label="Password*"
                        v-model="registration.password"
                        :type="'password'"
                        :rules="password_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      <v-text-field
                        label="Confirm password*"
                        v-model="passwordConfirmation"
                        :error-messages="passwordMatchError()"
                        :type="'password'"
                        :rules="confirm_password_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        label="Email*"
                        v-model="registration.email"
                        :rules="email_rules"
                        required
                      ></v-text-field>
                    </v-flex>

                    <v-flex xs12 sm6>
                      <v-text-field
                        type="number"
                        label="Age*"
                        v-model="registration.age"
                        :rules="age_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      <v-text-field
                        type="number"
                        label="Weight*"
                        v-model="registration.weight"
                        :rules="weight_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      <v-text-field
                        type="number"
                        label="Height*"
                        v-model="registration.height"
                        :rules="height_rules"
                        required
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                      Sex*
                      <v-radio-group v-model="radios" :mandatory="false">
                        <v-radio label="Male" value="male" required></v-radio>
                        <v-radio label="Female" value="female" required></v-radio>
                      </v-radio-group>
                    </v-flex>
                  </v-layout>
                </v-card-text>
                <small>*indicates required field</small>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="white" @click="back">Back</v-btn>
                  <v-btn
                    :disabled="!formValid"
                    color="primary"
                    @click="validateUser"
                  >Create an account</v-btn>
                  <v-btn color="error" @click="reset">Reset</v-btn>
                </v-card-actions>
              </v-form>
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
import Registration from "@/model/Registration.js";
import AuthService from "@/api-services/auth.service.js";

export default {
  name: "Registration",
  data: () => ({
    formValid: true,
    passwordConfirmation: null,
    firstname_rules: [v => !!v || "First name is required"],
    lastname_rules: [v => !!v || "Last name is required"],
    username_rules: [v => !!v || "Username is required"],
    password_rules: [
      v => !!v || "Password is required",
      v => (v && v.length >= 5) || "Password is too short"
    ],
    email_rules: [
      v => !!v || "Email is required",
      v =>
        /[0-9]*[A-Z]*[0-9]*[a-z]+[0-9]*[A-Z]*[0-9]*@[a-z]+\.[a-z]+/.test(v) ||
        "Please enter a valid email"
    ],
    age_rules: [v => !!v || "Age is required"],
    weight_rules: [
      v => !!v || "Weight is required",
      v => (v && v > 30) || "You need to be heavier than 30 kg.",
      v => (v && v > 30) || "You need to be lighter than 100 kg."
    ],
    height_rules: [
      v => !!v || "Height is required",
      v => (v && v > 130) || "You need to be higher than 130 cm."
    ],
    confirm_password_rules: [v => !!v || "Password confirmation is required"],
    registration: new Registration(),
    radios: "male",
    snackbar: {
      show: false,
      color: "",
      msg: ""
    }
  }),
  methods: {
    passwordMatchError() {
      return this.registration.password === this.passwordConfirmation
        ? ""
        : "Please enter a matching password";
    },
    reset() {
      this.$refs.form.reset();
    },
    validateUser() {
      if (this.$refs.form.validate()) {
        this.onSubmit();
      }
    },
    onSubmit() {
      console.log(this.registration);
      this.registration.sex = this.radios == "male";
      AuthService.register(this.registration)
        .then(response => {
          console.log(response);
          this.showSnackbar("Successful registration", "success");
          this.back();
        })
        .catch(error => {
          this.showSnackbar(error.response.data, "error");
        });
    },
    back() {
      this.$router.push("login");
    },
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
    }
  }
};
</script>