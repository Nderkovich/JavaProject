import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../components/Login.vue'
import Logout from '../components/Logout.vue'
import Documents from '../components/Documents.vue'
import DocumentView from "../components/DocumentView.vue"
import Registration from "../components/Registration.vue"
import SearchDocuments from "../components/SearchDocuments.vue"
import UploadDocument from "../components/Admin/UploadDocument.vue"

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            requiresVisitor: true
        }
    },
    {
        path: '/register',
        name: 'Regiser',
        component: Registration,
        meta: {
            requiresVisitor: true
        }
    },
    {
        path: '/logout',
        name: 'Logout',
        component: Logout,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/documents',
        name: 'Documents',
        component: Documents,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/search',
        name: 'Search',
        component: SearchDocuments,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/document/:id',
        name: 'DocumentView',
        component: DocumentView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/admin',
        name: 'Admin',
        component: UploadDocument,
        maeta: {
            requiresAdmin: true
        }
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
