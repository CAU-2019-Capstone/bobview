// https://vuex.vuejs.org/en/actions.html

import axios from 'axios'

export default {
    verifyLogin(context) {
        axios.post('http://127.0.0.1:8000/api/login/verify/', {
                username: context.state.userdata['username'],
                token: context.state.userdata['logintoken'],
            })
            .then(function(response) {
                console.log(response.data)
                if (response.data['result']) {
                    context.commit('setLogin')
                } else {
                    context.commit('logout')
                }
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
    }
}