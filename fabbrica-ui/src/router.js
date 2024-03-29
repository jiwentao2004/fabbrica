import Vue from 'vue'
import Router from 'vue-router'
import Page from './views/Page.vue'
import Auth from './views/Auth.vue'
import Login from './views/auth/Login.vue'
import User from './views/page/User.vue'
import UserForm from './views/page/UserForm.vue'
import Tenant from './views/page/Tenant.vue'
import TenantForm from './views/page/TenantForm.vue'
import Factory from './views/page/Factory.vue'
import FactoryForm from './views/page/FactoryForm.vue'
import MachineModel from './views/page/MachineModel.vue'
import MachineModelForm from './views/page/MachineModelForm.vue'
import Machine from './views/page/Machine.vue'
import MachineForm from './views/page/MachineForm.vue'
import Visualization from './views/page/Visualization.vue'
import VisualizationForm from './views/page/VisualizationForm.vue'
import Dashboard from './views/page/Dashboard.vue'
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
          path: '',
          name: 'dashboard',
          component: Dashboard
        },
        {
          path: 'visualizations',
          name: 'visualizations',
          component: Visualization
        },
        {
          path: 'visualizations/:id',
          name: 'visualization-form',
          component: VisualizationForm
        },
        {
          path: 'machines',
          name: 'machines',
          component: Machine
        },
        {
          path: 'machines/:id',
          name: 'machine-form',
          component: MachineForm
        },
        {
          path: 'machine-models',
          name: 'machine-models',
          component: MachineModel
        },
        {
          path: 'machine-models/:id',
          name: 'machine-model-form',
          component: MachineModelForm
        },
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
        },
        {
          path: 'users',
          name: 'users',
          component: User
        },
        {
          path: 'users/:id',
          name: 'user-form',
          component: UserForm
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
