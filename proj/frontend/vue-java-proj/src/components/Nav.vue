<template>
  <div id="nav" class="nav">
    <ul>
        <li><router-link  to="/documents">Documents</router-link></li>
        <li v-if="!loggedIn"><router-link  to="/login">Login</router-link></li>
        <li v-if="!loggedIn"><router-link  to="/register">Register</router-link></li>
        <li v-if="loggedIn"><router-link  to="/logout">Logout</router-link></li>
        <li v-if="loggedIn && isAdmin"><router-link  to="/admin">Admin</router-link></li>
        <li>
          <form action="#" class="search"  @submit.prevent="searchDocument">
              <input type="text" name="search" id="search" v-model="search">
              <button type="submit">Search</button>
          </form>
        </li>
    </ul>
  </div>
</template>

<script>
export default {
    computed: {
        loggedIn() {
            return this.$store.getters.loggedIn
        },
        isAdmin() {
          return this.$store.getters.isAdmin
        }
    },
    data() {
        return{
            search: '',
        }
    },
    beforeMount() {
      this.$store.dispatch('getPriveleges');
    },
    methods: {
        searchDocument(){
            this.$store.dispatch('search', {
                search: this.search,
            })
            .then(res => {
                this.search = '',
                this.$router.push({name: "Search"}).catch(err => {})
            })
        }
    }
};
</script>

<style scoped>
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #ffffff;
}

li {
  float: left;
    font-weight: bold;
}

li a {
  display: block;
  color: #42b983;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

li a:hover {
  background-color: rgb(236, 234, 234);
}

#nav {
  padding: 5px 10px;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
#search{
  display: inline-block;
}
</style>