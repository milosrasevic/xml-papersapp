<template>
  <div id="createReview">
    <create-review-form v-bind:blinded="blinded"></create-review-form>
    <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import CreateReviewForm from "@/components/CreateReviewForm";
// import store from "@/store";

export default {
  name: "Login",
  components: {
    CreateReviewForm: CreateReviewForm
  },
  // store: store,
  data: () => ({
    blinded: "STANDARD",
    snackbar: {
      show: false,
      color: "",
      msg: ""
    }
  }),
  created() {
    // todo change to passed property
    this.blinded = "STANDARD";
  },
  methods: {
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
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
