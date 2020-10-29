import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios"

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    if (!store.getters.loggedIn) {
      next({
        path: '/login'
      })
    } else {
      next()
    }
  } 
  else if (to.matched.some(record => record.meta.reuiresAdmin)){
    if (!store.getters.isAdmin){
      next({
        path: '/documents'
      })
    } else {
      next()
    }
  }
  else {
    next() // make sure to always call next()!
  }
})

Vue.prototype.$http = axios

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

