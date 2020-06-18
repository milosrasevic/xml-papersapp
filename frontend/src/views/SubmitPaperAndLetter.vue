<template>
  <div style="height: 100%; position: relative">
    <div style="height: 80%; position: relative">
      <v-layout row wrap justify-space-around style="height: 100%">
        <v-flex xs6 md6 lg5>
          <h1>Enter science paper</h1>
          <v-textarea class="my-area" solo name="input-7-4" label height="100%" no-resize v-model="sciencePaper"></v-textarea>
        </v-flex>
        <v-flex xs6 md6 lg5>
          <h1>Enter cover letter</h1>
          <v-textarea class="my-area" solo name="input-7-4" label height="100%" no-resize v-model="coverLetter"></v-textarea>
        </v-flex>
      </v-layout>
    </div>
    <v-btn
      color="success"
      style="position: absolute; bottom: 30px; right: 50px"
      large
      @click="submit"
    >Submit</v-btn>
    <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>

import SciencePapersService from "../api-services/science-papers.service";
import CoverLetterService from "../api-services/cover-letter.service";

export default {
  name: "SubmitPaperAndLetter",
  data() {
    return {
      sciencePaper: "",
      coverLetter: "",
      snackbar: {
        show: false,
        color: "",
        msg: ""
      }
    };
  },
  methods: {
    submit() {
      if (this.sciencePaper === "" || this.coverLetter === "") {
        this.snackbar.color = "error";
        this.snackbar.msg = "You must enter science paper and cover letter";
        this.snackbar.show = true;

        return;
      }

      SciencePapersService.submit(this.sciencePaper).then(() => {
        CoverLetterService.submit(this.coverLetter).then(() => {
        this.snackbar.color = "success";
        this.snackbar.msg = "Successfully submited science paper and cover letter.";
        this.snackbar.show = true;

        this.$router.push("/profile");
        })
        .catch(() => {
         this.snackbar.color = "error";
        this.snackbar.msg = "Error occured! Please try again.";
        this.snackbar.show = true;
      })
      }).catch(() => {
         this.snackbar.color = "error";
        this.snackbar.msg = "Error occured! Please try again.";
        this.snackbar.show = true;
      })

    }
  }
};
</script>

<style>
.my-area {
  height: 100% !important;
}

.my-area .v-input__control {
  height: 100% !important;
}
/* 
.v-input v-textarea theme--light v-text-field v-text-field--single-line v-text-field--solo v-text-field--is-booted v-text-field--enclosed {
  height: 100% !important;
}

.v-input__control {
  height: 100% !important;
} */
</style>