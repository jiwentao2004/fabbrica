import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    accessToken: localStorage.getItem('access_token') || '',
    refreshToken: localStorage.getItem('refresh_token') || '',
    user: JSON.parse(localStorage.getItem('user') || '{}')
  },
  getters: {
    isLoggedIn: (state) => !!state.accessToken,
  },
  mutations: {
    login(state, payload) {
      state.accessToken = payload.accessToken;
      state.refreshToken = payload.refreshToken;
      state.user = payload.loggedUser;
    },
    logout(state) {
      state.accessToken = '';
      state.refreshToken = '';
      state.user = {};
    }
  },
  actions: {
    login({ commit }, { loggedUser, accessToken, refreshToken }) {
      console.log('Login');
      localStorage.setItem('access_token', accessToken);
      localStorage.setItem('refresh_token', refreshToken);
      localStorage.setItem('user', JSON.stringify(loggedUser));
      commit('login', { loggedUser, accessToken, refreshToken });
    },
    register({ commit }, user) {
      console.log('Register');
    },
    logout({ commit }) {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      localStorage.removeItem('user');
      commit('logout');
      console.log('Logout');
    },
  }
})
