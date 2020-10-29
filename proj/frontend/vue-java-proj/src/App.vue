<template>
  <div id="app">
    <Nav />
    <Popup v-if="errorVisible"/>
    <router-view />
  </div>
</template>

<script>
import Popup from './components/Popup';
import Nav from "./components/Nav";

export default {
  
  components: {
    Nav,
    Popup
  },

  computed: {
    errorVisible() {
      return this.$store.getters.getErrorVisible
    }
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

button {
  background-color: #4caf50; /* Green */
  border: none;
  border-radius: 2px;
  color: white;
  padding: 7px 7px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 15px;
  font-weight: bold;
}
</style>
