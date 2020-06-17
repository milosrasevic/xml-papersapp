<template>
  <div><span>logging out...</span></div>
</template>

<script>

import LogoutService from "@/api-services/auth.service.js";

export default {
  name: "Logout",
  beforeCreate() {
    LogoutService.logout();
    this.$router.push("/");
  }
}
</script>

<style>

</style>
