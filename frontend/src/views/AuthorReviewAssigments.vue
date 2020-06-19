<template>
  <div style="height: 100%; position: relative">
    <v-data-table :headers="headers" :items="myAssigments" :items-per-page="5" class="elevation-1">
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Review Assigments</v-toolbar-title>
          <v-spacer></v-spacer>
        </v-toolbar>
      </template>
      <template v-slot:item.accept="{item}">
        <v-btn color="success" @click="accept(item)" style="margin-left: 5px">Accept</v-btn>
        <v-btn color="error" @click="deny(item)" style="margin-left: 5px">Deny</v-btn>
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-divider></v-divider>
    <v-data-table
      :headers="headers_papers"
      :items="myPapersToReview"
      :items-per-page="5"
      class="elevation-1"
    >
    <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Science papers to review</v-toolbar-title>
          <v-spacer></v-spacer>
        </v-toolbar>
      </template>
      <template v-slot:item.review="{item}">
        <v-btn color="success" @click="goTo(item.title)" style="margin-left: 5px">Create review</v-btn>
      </template>
    </v-data-table>
    <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import ReviewAssigmentService from "../api-services/review-assigment.service";
import ReviewService from "../api-services/review.service";
// import CoverLetterService from "../api-services/cover-letter.service";

export default {
  name: "AuthorReviewAssigments",
  data() {
    return {
      assigments: [],
      papersToReview: [],
      coverLetter: "",
      headers: [
        { text: "Id", value: "id", align: "center" },
        {
          text: "Title",
          align: "start",
          sortable: false,
          value: "title"
        },
        { text: "Blinded", value: "blinded", align: "center" },
        { text: "Accept", value: "accept", align: "center" }
      ],
      headers_papers: [
        {
          text: "Title",
          align: "start",
          sortable: false,
          value: "title"
        },
        { text: "Authors", value: "authors", align: "center" },
        { text: "Type", value: "type", align: "center" },
        { text: "Keywords", value: "keywords", align: "center" },
        { text: "State", value: "state", align: "center" },
        { text: "Review", value: "review", align: "center" }
      ],
      snackbar: {
        show: false,
        color: "",
        msg: ""
      }
    };
  },
  computed: {
    myAssigments: function() {
      return this.assigments;
    },
    myPapersToReview: function() {
      return this.papersToReview;
    },
  },
  created() {
    this.fetchData();
  },
  methods: {
    goTo(url) {
      this.$router.push("/createReview/" + url);
    },
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
    },
    accept(item) {
      console.log(item);
      ReviewService.acceptReview(item.id, true).then(response => {
        // this.$router.push("/createReview/" + response.data.sciencePaperTitle);
        console.log(response.data);
        this.showSnackbar("You have successfully accepted a review on paper with title: " + item.title, "success")
        this.assigments = [];
        this.papersToReview = [];
        this.fetchData();
      });
    },
    deny(item) {
      console.log(item);
      ReviewService.acceptReview(item.id, false).then(response => {
        // this.$router.push("/createReview/" + response.data.sciencePaperTitle);
        console.log(response.data);
        this.showSnackbar("You have successfully denied a review on paper with title: " + item.title, "success")
        this.assigments = [];
        this.papersToReview = [];
        this.fetchData();
      });
    },
    fetchData() {
      ReviewAssigmentService.getMyAssigments().then(response => {
        console.log(response.data);
        response.data.forEach(el => {
          if (el.accept == undefined) {
            let assigment = {
              id: el.id,
              title: el.sciencePaperTitle,
              blinded: el.blinded
            };

            this.assigments.push(assigment);
          }
        });
      });

      ReviewAssigmentService.getPapersToReview().then(response => {
        console.log(response.data);
        response.data.forEach(el => {
          let authors = "";
          el.authors.author.forEach(author => {
            authors += author.firstName + " " + author.lastName + ", ";
          });

          if (authors !== "") {
            authors = authors.substr(0, authors.length - 2);
          }

          let keywords = "";
          el.abstract.keywords.keyword.forEach(kw => {
            keywords += kw + ", ";
          });

          if (keywords !== "") {
            keywords = keywords.substr(0, keywords.length - 2);
          }

          let sp = {
            title: el.title,
            type: el.abstract.type,
            authors,
            keywords,
            state: el.state,
            id: el.id
          };

          this.papersToReview.push(sp);
        });
      });
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