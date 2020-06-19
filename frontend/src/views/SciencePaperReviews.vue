<template>
  <v-container>
    <v-btn @click="$router.push('/')">Back</v-btn>
    <h1>Reviews for {{paperTitle}}</h1>
    <v-data-table :headers="headers" :items="reviews" :items-per-page="5" class="elevation-1">
      <template v-slot:item.reviewer="{item}">
        <td>{{item.reviewer.firstName}} {{item.reviewer.lastName}}</td>
      </template>
      <template v-slot:item.view="{item}">
        <a
          :href="getDownloadLink('PDF', item)"
          download
          style="text-decoration: none"
          target="_blank"
        >
          <v-btn color="success"  style="margin-left: 5px">PDF</v-btn>
        </a>
        <a
          :href="getDownloadLink('HTML', item)"
          download
          style="text-decoration: none"
          target="_blank"
        >
          <v-btn color="success"  style="margin-left: 5px">HTML</v-btn>
        </a>
        <a
          :href="getDownloadLink('XML', item)"
          download
          style="text-decoration: none"
          target="_blank"
        >
          <v-btn color="success"  style="margin-left: 5px">XML</v-btn>
        </a>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import ReviewService from "../api-services/review.service";

export default {
  name: "SciecenpaperReviews",
  data() {
    return {
      reviews: [],
      paperTitle: "",
      headers: [
        {
          text: "Reviewer",
          align: "start",
          sortable: false,
          value: "reviewer"
        },
        { text: "Blinded", value: "blinded", align: "center" },
        { text: "Date created", value: "dateCreated", align: "center" },
        { text: "View", value: "view", align: "center" }
      ]
    };
  },
  created() {
    let paperTitle = this.$router.history.current.params.title;
    this.paperTitle = paperTitle;
    let paperId = this.$router.history.current.params.id;
    this.getReviews(paperId);
  },
  methods: {
    getReviews(paperId) {
      ReviewService.getReviewsForSciencePaperId(paperId).then(res => {
        this.reviews = res.data;
        console.log(this.reviews);
      });
    },
    getDownloadLink(docType, item) {
      if (item.id === null) {
        return "";
      }

      let id = item.id.split("http://www.tim12.com/review/")[1];
      id = "http://localhost:8081/api/review/get" + docType + "/" + id;
      console.log(id);
      return id;
    },
  },
  
};
</script>

<style>
</style>