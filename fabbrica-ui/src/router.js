import Vue from 'vue'
import Router from 'vue-router'
import Page from './views/Page.vue'
import Auth from './views/Auth.vue'
import Login from './views/auth/Login.vue'
import Tenant from './views/page/Tenant.vue'
import TenantForm from './views/page/TenantForm.vue'
import Factory from './views/page/Factory.vue'
import FactoryForm from './views/page/FactoryForm.vue'
import store from './store'


Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Page,
      meta: {
        requiresAuth: true,
      },
      children: [
        {
          path: 'factories',
          name: 'factories',
          component: Factory
        },
        {
          path: 'factories/:id',
          name: 'factory-form',
          component: FactoryForm
        },
        {
          path: 'tenants',
          name: 'tenants',
          component: Tenant
        },
        {
          path: 'tenants/:id',
          name: 'tenant-form',
          component: TenantForm
        }
      ]
    },
    {
      path: '/auth',
      name: 'auth',
      component: Auth,
      children: [
        {
          path: 'login',
          name: 'login',
          component: Login
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (store.getters.isLoggedIn) {
      next();
      return;
    }
    next('/auth/login');
  } else {
    next();
  }
});

export default router;
