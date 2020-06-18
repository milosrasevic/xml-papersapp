<template>
  <v-container>
    <v-btn @click="goTo('/createReview/title0')">Create review</v-btn>
  </v-container>
</template>

<script>

export default {
  name: "Home",
  methods: {
    goTo(url) {
      this.$router.push(url);
    },
  }
};
</script>
