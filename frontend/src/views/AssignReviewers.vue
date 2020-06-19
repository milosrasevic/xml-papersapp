<template>
  <div id="assignReviewers">

    <v-data-table
        v-model="selected"
        :headers="headers"
        :items="reviewers"
        item-key="email"
        show-select
        class="elevation-1"
    >  
        <!-- <template v-slot:item.blinded="props">
            <v-edit-dialog
            :return-value.sync="props.item.blinded"
            @save="save"
            @cancel="cancel"
            > {{ props.item.blinded }}
            <template v-slot:input>
                <v-select
                    v-model="props.item.blinded"
                    label="Edit"
                    counter
                    >
                    <option value="standard">STANDARD</option>
                    <option value="doubleBlinded">DOUBLE_BLINDED</option>
                </v-select>
            </template>
            </v-edit-dialog>
        </template> -->
        <template v-slot:item.blinded="{item}">
        <!-- <a :href="assignReviewers(item)" style="text-decoration: none" target="_blank"> -->
          <v-select v-model="item.blinded" :items="items"></v-select>
          
        <!-- </a> -->
      </template>

    </v-data-table>
    <div class="buttons">
        <v-btn class="button" @click="goBack()">Back</v-btn>
        <v-btn color="success" :disabled="selectedEmpty" @click="assignToSelected()">Assign</v-btn>
    </div>
    <v-snackbar v-model="snackbar.show" :timeout="5000" :color="snackbar.color" :top="true">
      {{snackbar.msg}}
      <v-btn dark @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>

import UserService from '../api-services/user.service';
import ReviewService from '../api-services/review.service';

export default {
  name: "AssignReviewers",
  data() {
      return {
          reviewers: [],
          selected: [],
          headers: [{
                text: "Firstname",
                align: "start",
                sortable: false,
                value: "firstName"
                },
                { text: "Lastname", value: "lastName", align: "center" },
                { text: "Email", value: "email", align: "center" },
                { text: "Blinded", value: "blinded", align: "center" }
           ],
           items: ["STANDARD", "DOUBLE_BLINDED", "AUTHOR_BLINDED", "REVIEWER_BLINDED"],
           snackbar: {
            show: false,
            color: "",
            msg: ""
          }
        };
  },
  created() {
    UserService.getUsers().then((res) => {
        res.data.user.map(user => {
            this.reviewers.push({...user, blinded: "STANDARD"})
        })
        console.log(res.data.user);
    });
  },
  computed: {
      selectedEmpty() {
        return this.selected.length == 0;
    }
  },
  methods: {
    assignReviewer(item) {
        item['checked'] = true;
        console.log(item);
    },
    assignToSelected() {
        let emailsAndBlinded = []; 

        this.selected.map(user => {
            emailsAndBlinded.push({email: user.email,blinded: user.blinded});
        });

        let data = {
            title: this.$route.params.sciencePaperTitle,
            reviewAssignmentDTO: emailsAndBlinded
        }

        this.selected = [];

        console.log(data);
        
        ReviewService.assignReviewers(data).then(() => {
            this.showSnackbar("Success", "success");
            this.goBack();
        });
    },
    goBack() {
        this.$router.push("/");
    },
    showSnackbar(message, color) {
      this.snackbar.color = color;
      this.snackbar.msg = message;
      this.snackbar.show = true;
    }
  }
};
</script>

<style>

.buttons {
    margin-top: 3%;
    text-align: center;
}

.button {
    margin-right: 2%;
}

</style>