// https://vuex.vuejs.org/en/actions.html

import axios from 'axios'

export default {
    verifyLogin(context) {
        axios.post('http://localhost:8000/api/login/verify/', {
                username: context.state.userdata['username'],
                token: context.state.userdata['logintoken'],
            })
            .then((result) => {
                console.log(result.data)
                if (result.data['result']) {
                    context.commit('setLogin')
                } else {
                    context.commit('logout')
                }
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
    },
    getUserInfo(context) {
        axios.get('http://localhost:8000/api/userinfo/0/?username=' + context.state.userdata['username'])
            .then((result) => {
                console.log("get response : " + result.data)
                if (result.data['is_owner']) {
                    context.commit('setIsowner')
                }
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
    }

}