<template>
<div style="margin: 0 100px 0 100px">
    <h1>Science papers</h1>
    <v-text-field
      style="width: 500px"
      v-model="searchParam.text"
      label="Search for title, keywoards, authors, text"
      required
    ></v-text-field>
    <v-layout row wrap>
      <v-flex xs3 md3>
        <v-text-field
          style="margin-left: 10px; width: 200px"
          v-model="searchParam.dateFrom"
          label="Date from"
          required
        ></v-text-field>
      </v-flex>
      <v-flex xs3 md3>
        <v-text-field
          style="margin-left: 20px; width: 200px"
          v-model="searchParam.dateTo"
          label="Date to"
          required
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-select
      v-if="isEditor"
      style="width: 200px"
      :items="stateItems"
      v-model="searchParam.state"
      label="State"
    ></v-select>
    <v-layout row wrap>
      <v-btn style="margin: 10px" color="success" @click="search">Search</v-btn>
      <v-btn style="margin: 10px; color: red;" color @click="discardSearch">Discard</v-btn>
    </v-layout>
    <h1 v-if="isNonAuthUser">Science papers</h1>
    <v-data-table
      :headers="getHeaders()"
      :items="sciencePapers"
      :items-per-page="5"
      class="elevation-1"
    >
      <template v-slot:item.download="{item}">
        <a :href="getDownloadLink('PDF', item)" download style="text-decoration: none" target="_blank">
          <v-btn color="success" @click="download('pdf', item)" style="margin-left: 5px">PDF</v-btn>
        </a>
        <a :href="getDownloadLink('HTML', item)" download style="text-decoration: none" target="_blank">
          <v-btn color="success" @click="download('html', item)" style="margin-left: 5px">HTML</v-btn>
        </a>
        <a :href="getDownloadLink('XML', item)" download style="text-decoration: none" target="_blank">
          <v-btn color="success" @click="download('xml', item)" style="margin-left: 5px">XML</v-btn>
        </a>
      </template>
      <template v-slot:item.reviews="{item}">
        <v-btn @click="viewReviews(item)">
          <v-icon>mdi-playlist-check</v-icon>
        </v-btn>
      </template>
      <template v-slot:item.actions="{item}">
        <v-btn v-if="stateWaiting(item.state)" color="success" @click="assignReviewers(item)" style="margin-left: 5px">Assign reviewers</v-btn>
        <div v-if="item.state === 'WAITING_FOR_APPROVAL'">
            <v-btn color="success" @click="action('true', item)" style="margin-left: 5px">Accept</v-btn>
        <v-btn color="warning"  style="margin-left: 5px">Revise</v-btn>
        <v-btn color="error" @click="action('false', item)" style="margin-left: 5px">Reject</v-btn>
        </div>
      </template>
    </v-data-table>
     <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>
<script>
import SciencePapersService from "../api-services/science-papers.service";

export default {
  name: "Home",
  components: {},
  data() {
    return {
      headersNonAuthUser: [
        {
          text: "Title",
          align: "start",
          sortable: false,
          value: "title"
        },
        { text: "Authors", value: "authors", align: "center" },
        { text: "Type", value: "type", align: "center" },
        { text: "Keywords", value: "keywords", align: "center" },
        { text: "Download", value: "download", align: "center" }
      ],
      headersAuthor: [
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
        { text: "Download", value: "download", align: "center" }
      ],
       headersEditor: [
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
        { text: "Download", value: "download", align: "center" },
        { text: "Reviews", value: "reviews", align: "center" },
        { text: "Actions", value: "actions", align: "center" }
      ],
      sciencePapers: [],
      searchParam: {
        text: "",
        dateFrom: "",
        dateTo: "",
        state: ""
      },
      stateItems: [
        { text: "waiting", value: "waiting" },
        { text: "review_in_progress", value: "review_in_progress" },
        { text: "rejected", value: "rejected" },
        { text: "accepted", value: "accepted" },
        { text: "deleted", value: "deleted" },
        { text: "waiting_for_approval", value: "waiting_for_approval" },
        { text: "revision_needed", value: "revision_needed" }
      ],
       snackbar: {
      show: false,
      color: "",
      msg: ""
    }
    };
  },
  computed: {
    isNonAuthUser() {
      return this.$store.getters.isNonAuthUser;
    },
    isAuthor() {
      return this.$store.getters.isAuthor;
    },
    isEditor() {
      return this.$store.getters.isEditor;
    }
  },
  created() {
    this.search();
  },
  methods: {
    getAuthors() {
      return "";
    },
    search() {
      this.sciencePapers = [];
      SciencePapersService.getSciencePapers(
        this.searchParam,
        this.isEditor
      ).then(res => {
        res.data.forEach(el => {
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

          this.sciencePapers.push(sp);
        });
      });
    },
    discardSearch() {
      this.searchParam = {
        text: "",
        dateFrom: "",
        dateTo: "",
        state: ""
      };
      this.search();
    },
    download(fileType, item) {
      console.log(item);
    },
    getDownloadLink(docType, item) {

      console.log(item);
      if (item.title === null) {
        return "";
      }

      let id = "http://localhost:8081/api/science-paper/get" + docType + "/" + item.title;
      console.log(id);
      return id;
    },
    assignReviewers(item) {
      this.$router.push("/assign-reviewers/" + item.title);
      console.log(item);
    },
    stateWaiting(state) {
      return state == "WAITING";
    },
    getHeaders() {
      if (this.isEditor) {
        return this.headersEditor;
      } else if (this.isAuthor) {
        return this.headersAuthor;
      }

      return this.headersNonAuthUser;
    },
    viewReviews(item) {
      let id = item.id.split("http://www.tim12.com/science_paper/")[1];
      this.$router.push("/science-paper/" + id + "/" +  item.title +  "/reviews");
    },
    action(accepted, item) {
    
      SciencePapersService.decide(item.title, accepted).then(() => {
        this.showSnackbar("Science paper successfully modified", "success");
        this.search();
      }).catch(() => {
        this.showSnackbar("Error occured! Try again.", "error");
      })
    },
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
    },
  }
};
</script>
