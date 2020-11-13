import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios"

Vue.use(Vuex)

const api = 'http://localhost:8080/api';

export default new Vuex.Store({
    state: {
        token: localStorage.getItem('token') || null,
        userId: localStorage.getItem('userId') || null,
        isAdmin: false,
        documents: [],
        document: {},
        comments: [],
        searchResults: [],
        rating: null,
        errorVisible: false,
        errorMessage: ''
    },
    mutations: {
        setToken(state, token) {
            state.token = token
        },
        destroyToken(state) {
            state.token = null
            state.userId = null
            localStorage.removeItem('token')
            localStorage.removeItem('userId')
        },

        setDocuments(state, documents) {
            state.documents = documents
        },
        setDocument(state, document) {
            state.document = document;
            state.rating = document.rating;
        },
        setComments(state, comments) {
            state.comments = comments;
        },
        setUserId(state, id) {
            state.userId = id;
        },
        setsearchResults(state, docs) {
            state.searchResults = docs;
        },
        setIsAdmin(state, admin){
            state.isAdmin = admin;
        },
        setErrorMessage(state, message){
            state.errorMessage = message;
        },
        setErrorVisible(state){
            state.errorVisible = !state.errorVisible;
        }
    },
    getters: {
        loggedIn(state) {
            return state.token !== null
        },
        getToken(state) {
            return state.token;
        },
        getDocuments(state) {
            return state.documents;
        },
        getDocument(state) {
            return state.document;
        },
        getComments(state) {
            return state.comments;
        },
        getRating(state) {
            return state.rating;
        },
        getUserId(state) {
            return state.userId;
        },
        getsearchResults(state){
            return state.searchResults;
        },
        isAdmin(state){
            if (state.token)
                return state.isAdmin;
            else
                return false;
        },
        getErrorMessage(state){
            return state.errorMessage
        },
        getErrorVisible(state){
            return state.errorVisible
        }
    },
    actions: {
        getToken(context, credentials) {
            return axios.post(api + '/auth/login', {
                username: credentials.username,
                password: credentials.password
            })
            .then(res => {
                localStorage.setItem('token', res.data.token)
                localStorage.setItem('userId', res.data.id)
                context.commit('setToken', res.data.token)
                context.commit('setUserId', res.data.id)
                context.dispatch('getPriveleges')
            })
            .catch((e) => {
                context.commit('setErrorMessage', e.response.data)
                context.commit('setErrorVisible')
                console.log(e.response.data)})
        },
        destroyToken(context) {
            context.commit('destroyToken')
            context.commit('setIsAdmin', false)
        },
        getPriveleges(context) {
            axios.get(api + '/auth/isadmin', {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                } 
            })
            .then(res => context.commit('setIsAdmin', res.data.isAdmin))
        },

        register(context, credentials) {
            return new Promise((resolve, reject) => {
                axios.post(api + '/auth/register', {
                    username: credentials.username,
                    password: credentials.password
                })
                .catch((e) => {
                    context.commit('setErrorMessage', e.response.data)
                    context.commit('setErrorVisible')
                    console.log(e.response.data)
                    reject()    
                })
                .then(() => resolve())
            })
        },

        getDocuments(context) {
            axios.get(api + '/documents', {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                }
            })
            .then(res => context.commit('setDocuments', res.data))
        },
        getDocument(context, id) {
            axios.get(api + '/documents/' + id, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                }
            })
            .then(res => {
                context.commit('setDocument', res.data)
                context.dispatch('getComments', id)
            })
        },
        getComments(context, id) {
            axios.get(api + `/documents/${id}/comments`, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                }
            })
            .then(res => context.commit('setComments', res.data))
        },
        downloadDocument(context, id) {
            axios.get(api + `/documents/${id}/download`, {
                responseType: 'blob',
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken,
                },
            })
            .then(res => {
                console.log(res.headers)
                let filename = res.headers['content-disposition'].split('=')[1].split('.')[0];
                console.log(filename);
                const link = document.createElement('a')
                link.href = URL.createObjectURL(new File([res.data], filename, {type: res.data.type}))
                link.download = filename
                link.click()
                URL.revokeObjectURL(link.href)
            })
        },

        addComment(context, comment) {
            axios.post(api + `/documents/${comment.document}/comments`, {comment: comment.text}, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                },
            })
            .then(() => context.dispatch('getComments', comment.document))
        },
        updateComment(context, comment) {
            axios.put(api + `/documents/${comment.docId}/comments/${comment.commId}`, {comment: comment.text}, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                },
            })
            .then(() => context.dispatch('getComments', comment.docId))
        },
        deleteComment(context, data) {
            axios.delete(api + `/documents/${data.doc}/comments/${data.comm}`, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                }, 
            })
            .then(() => context.dispatch('getComments', data.doc))
        },

        rateDocument(context, rate) {
            axios.post(api + `/documents/${rate.document}/rating`, {rating: rate.rating}, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                },
            })
            .then(() => context.dispatch('getDocument', rate.document))
        },
        deleteDocument(context, docId){
            return axios.delete(api + `/admin/document/${docId}`, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken
                },
            })
            .then(() => context.dispatch('getDocuments'))
        },

        search(context, search){
            return axios.post(api + '/documents/search', {search: search.search}, {
                headers: {
                    'Authorization': 'Bearer ' + context.getters.getToken,
                },
            }).then(res => context.commit('setsearchResults', res.data));
        },

        uploadDocument(context, doc){
            let formData = new FormData();
            formData.append("document", doc.doc);
            formData.append('desc', doc.desc)
            console.log(formData);
            return new Promise((resolve, reject) => {
                axios.post(api + "/admin/upload", formData, 
                {headers: {
                    "Content-Type": 'multipart/form-data;boundary=gc0p4Jq0M2Yt08jU534c0p',
                    Authorization: 'Bearer ' +  context.getters.getToken
                  }})
                .then(function (result) {
                    console.log(result);
                    return resolve(result);
                }, function (error) {
                    console.log(error);
                });
            })
        }
    },
    modules: {
    }
})
