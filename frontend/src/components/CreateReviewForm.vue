<template>
  <v-card class="mx-auto" color="dark" dark shaped>
    <v-card-title>
      <v-icon large left>mdi-message-draw</v-icon>
      <span class="title font-weight-light">
        <h1>{{myTitle}}</h1>
      </span>
      <template>
        <a :href="getDownloadLink('PDF')" download style="text-decoration: none" target="_blank">
          <v-btn color="black" @click="download('pdf')" style="margin-left: 5px">PDF</v-btn>
        </a>
        <a :href="getDownloadLink('HTML')" download style="text-decoration: none" target="_blank">
          <v-btn color="black" @click="download('html')" style="margin-left: 5px">HTML</v-btn>
        </a>
        <a :href="getDownloadLink('XML')" download style="text-decoration: none" target="_blank">
          <v-btn color="black" @click="download('xml')" style="margin-left: 5px">XML</v-btn>
        </a>
      </template>
    </v-card-title>
    <v-card-text class="text-xs-center justify-center" style="max-width: 100%">
      <div v-if="showAuthor">
        <h1>Authors</h1>
        <v-container v-for="author in authors" :key="author.email">
          Author
          <span style="font-weight: bold;">{{author.firstName}} {{author.lastName}}</span> Email:
          <span style="font-weight: bold;">{{author.email}}</span>
        </v-container>
      </div>
      <v-divider></v-divider>
      <v-select :items="statusItems" label="Overall recomendation" v-model="status"></v-select>

      <v-container fluid>
        <v-data-iterator
          :items="myComments"
          :items-per-page.sync="itemsPerPage"
          hide-default-footer
        >
          <template v-slot:header>
            <v-toolbar class="mb-2" color="indigo darken-5" dark flat>
              <v-toolbar-title>Comments on science paper paragraphs</v-toolbar-title>
              <template v-slot:extension>
                <v-btn bottom absolute @click="dialog = !dialog">
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </template>
            </v-toolbar>
          </template>

          <template v-slot:default="props">
            <v-row>
              <v-col
                v-for="comment in props.items"
                :key="comment.paragraphRef"
                sm="6"
                md="4"
                lg="3"
              >
                <v-card>
                  <v-card-title class="subheading font-weight-bold">{{ comment.paragraphRef }}</v-card-title>

                  <v-divider></v-divider>

                  <v-list dense>
                    <v-list-item>
                      <v-list-item-content>Text:</v-list-item-content>
                      <v-list-item-content class="align-end">{{ comment.value }}</v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-card>
              </v-col>
            </v-row>
          </template>
          <template v-slot:header>
            <v-toolbar class="mb-2" color="indigo darken-5" dark flat>
              <v-toolbar-title>Comments on science paper paragraphs</v-toolbar-title>
              <template v-slot:extension>
                <v-btn bottom absolute @click="dialog = !dialog">
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </template>
            </v-toolbar>
          </template>
        </v-data-iterator>
      </v-container>

      <v-container fluid>
        <v-textarea
          name="input-7-1"
          filled
          label="Overall opinion"
          auto-grow
          v-model="overallOpinion"
        ></v-textarea>
      </v-container>

      <v-container fluid>
        <v-textarea
          name="input-7-1"
          filled
          label="Final decision"
          auto-grow
          v-model="finalDecision"
        ></v-textarea>
      </v-container>
    </v-card-text>

    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>
          <v-select :items="paragraphRefs" label="Paragraph id" v-model="paragraphRef"></v-select>
        </v-card-title>
        <v-card-text>
          <v-container fluid>
            <v-textarea
              name="commentInput"
              filled
              label="Your comment"
              auto-grow
              v-model="commentText"
              :disabled="paragraphRef == ''"
            ></v-textarea>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn text color="primary" @click="submitComment()">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-card-actions>
      <v-btn class="d-inline-block" @click="createReview()">Create review</v-btn>
    </v-card-actions>
  </v-card>
</template>
<script>

import SciencePapersService from "../api-services/science-papers.service";
import ReviewService from "../api-services/review.service";
import store from "@/store";

export default {
  name: "CreateReviewForm",
  props: ["blinded"],
  data: () => ({
    statusItems: ["ACCEPTED", "REJECTED", "REVISED"],
    dialog: false,
    itemsPerPage: 4,
    paragraphRefs: [],
    paragraphIds: [],
    comments: [],
    authors: [],

      
    paragraphRef: "",
    commentText: "",
    status: "ACCEPTED",
    finalDecision: "This is why i made final decision.",
    overallOpinion: "This is my overall opinion.",
    sciencePaperId: "",
    sciPaperTitle: "",
    sciencePaper: {},

    event: "paperChange"
  }),
  created() {
    this.scipaperTitle = this.$route.params.scipaperTitle;
    console.log("is blinded:");
    console.log(this.myBlinded);

    SciencePapersService.get(this.scipaperTitle).then(response => {
      console.log(response.data);
      this.sciencePaper = response.data;
      

      this.sciencePaper.authors.author.forEach(author => {
        this.authors.push(author);
      });

      this.sciencePaper.content.section.forEach(section => {
        section.paragraph.forEach(paragraph => {
          this.paragraphRefs.push(paragraph.id.substring(21));
          this.paragraphIds.push(paragraph.id);
        });
      });
    });
  },
  computed: {
    myComments: function() {
      return this.comments;
    },
    mySciencePaper: function() {
      return this.sciencePaper;
    },
    myBlinded: function() {
      return this.blinded;
    },
    myTitle: function() {
      return this.scipaperTitle;
    },
    showAuthor: function() {
      if (
        this.blinded == "REVIEWER_BLINDED" ||
        this.blinded == "DOUBLE_BLINDED"
      ) {
        return false;
      } else {
        return true;
      }
    }
  },
  methods: {
    getDownloadLink(docType) {
      if(this.sciencePaper.id === undefined || this.sciencePaper.id===null) {
        return "";
      }else {

        let id = "http://localhost:8081/api/science-paper/get" + docType + "/" + this.sciencePaper.title;
        console.log(id);
        return id;
      }
    },
    download(fileType, item) {
      console.log(item);
    },
    submitComment() {
      this.dialog = false;

      let comment = {
        paragraphRef: this.paragraphRef,
        value: this.commentText
      };
      const index = this.paragraphRefs.indexOf(comment.paragraphRef);
      if (index > -1) {
        this.paragraphRefs.splice(index, 1);
      }

      this.comments.push(comment);
      this.commentText = "";
    },



    createReview() {

      console.log(store.state.user);

      let tAuthors = [];
      this.authors.forEach(author => {
          tAuthors.push({
            firstName: author.firstName,
            lastName: author.lastName,
            email: author.email
          })
      });

      let review = {
        authors: {
          author: tAuthors
        },
        reviewer: {
          firstName: store.state.user.firstName,
          lastName: store.state.user.lastName,
          email: store.state.user.email
        },
        comments: {
          comment: this.comments
        },
        finalDecision: this.finalDecision,
        overallOpinion: this.overallOpinion,
        status: this.status,
        blinded: this.blinded,
        sciencePaperId: this.sciencePaper.id
      };
      console.log(review);

      ReviewService.create(review).then(response => {
          console.log(response.data);
          this.$router.push("/");
      })
    }
  }
};
</script>
<style>
.pre-formatted {
  white-space: pre;
}
</style>