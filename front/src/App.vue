<template>
  <!--<div id="app">-->
    <!--<img src="./assets/logo.png">-->
    <!--<router-view/>-->
  <!--</div>-->

  <div id="app">
    <div class="container">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import '../node_modules/bootstrap/dist/css/bootstrap.css'
import '../node_modules/bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
