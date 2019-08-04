<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el label="User" icon="people" :to="{ name: 'users' }" />
          <q-breadcrumbs-el label="New" icon="add" />
        </q-breadcrumbs>
        <q-space />
        <q-btn v-if="edit" unelevated color="light-blue-6 q-mr-sm" @click="save">Save</q-btn>
        <q-btn v-if="!edit" unelevated color="light-blue-6 q-mr-sm" @click="enableEdit">Edit</q-btn>
        <q-btn v-if="edit" unelevated color="blue-grey-6" @click="cancel">Cancel</q-btn>
      </q-toolbar>
      <div class="row q-pa-md flex">
        <q-card class="full-width q-pa-md">
          <div class="row q-gutter-md">
            <div class="col-md-4 col-sm-6">
              <q-input
                outlined
                v-model="data.username"
                label="Username"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input outlined v-model="data.name" label="Name" class="q-mb-sm" :disable="!edit" />
              <q-input
                outlined
                v-model="password"
                label="Password"
                class="q-mb-sm"
                :disable="!edit"
                type="password"
              />
              <q-input
                outlined
                v-model="confirmPassword"
                label="Confirm Password"
                class="q-mb-sm"
                :disable="!edit"
                type="password"
              />
            </div>
            <div class="col-md-4 col-sm-6">
              <q-select
                outlined
                v-model="selectedTenant"
                use-input
                hide-selected
                fill-input
                input-debounce="0"
                label="Tenant"
                :options="tenantOptions"
                option-label="name"
                option-value="id"
                emit-value
                map-options
                @filter="tenantFilter"
                @input="tenantInput"
                :disable="!edit"
                class="q-mb-sm"
              >
                <template v-slot:option="scope">
                  <q-item v-bind="scope.itemProps" v-on="scope.itemEvents">
                    <q-item-section>
                      <q-item-label caption>{{scope.opt.code}}</q-item-label>
                      <q-item-label>{{ scope.opt.name }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </template>
                <template v-slot:no-option>
                  <q-item>
                    <q-item-section class="text-grey">No Tenants</q-item-section>
                  </q-item>
                </template>
              </q-select>
              <q-list
                bordered
                separator
                v-if="data.tenants && data.tenants.length > 0"
                class="rounded-borders"
                v-model="rerender"
              >
                <q-item v-for="(tenant, index) in data.tenants" :key="index" clickable v-ripple>
                  <q-item-section avatar>
                    <q-avatar color="primary" text-color="white">{{ tenant.name.charAt(0) }}</q-avatar>
                  </q-item-section>

                  <q-item-section>
                    <q-item-label>{{ tenant.name }}</q-item-label>
                    <q-item-label caption lines="1">{{ tenant.code }}</q-item-label>
                  </q-item-section>

                  <q-item-section side>
                    <q-btn
                      flat
                      outlined
                      icon="close"
                      color="red-6"
                      @click="removeTenant(index)"
                      :disable="!edit"
                      v-show="edit"
                    />
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </q-card>
      </div>
    </q-form>
  </q-page>
</template>

<script>
import Vue from "vue";
import Repository from "../../repository";
export default {
    data() {
        return {
            data: {
                name: "",
                code: ""
            },
            password: "",
            confirmPassword: "",
            selectedTenant: {},
            tenantOptions: [],
            dataCopy: undefined,
            edit: false,
            loading: false,
            rerender: false,
            repository: new Repository("users", this.$http),
            tenantRepository: new Repository("tenants", this.$http)
        };
    },
    beforeMount() {
        if (this.$route.params.id != "new") {
            this.repository
                .get(this.$route.params.id)
                .then(response => {
                    this.data = response.data;
                    this.data.password = "";
                })
                .catch(err => {
                    console.log(err);
                });
        } else {
            this.edit = true;
        }
    },
    methods: {
        cancel() {
            if (this.$route.params.id != "new") {
                this.edit = false;
                this.data = this.dataCopy;
            } else {
                this.$router.go(-1);
            }
        },
        save() {
            this.$q.loading.show();
            if (this.password !== "") {
                if (this.password !== this.confirmPassword) {
                    this.$q.notify({
                        color: "red-6",
                        message: "Passwords do not match!",
                        position: "top-right",
                        timeout: 3000
                    });
                    this.$q.loading.hide();
                    return;
                } else {
                    this.data.password = this.password;
                }
            }
            this.repository
                .save(this.data)
                .then(response => {
                    if (response.status == 200) {
                        this.$q.loading.hide();
                        this.$router.go(-1);
                    }
                })
                .catch(err => {
                    this.loading = false;
                    console.log(err);
                });
        },
        enableEdit() {
            this.edit = true;
            this.dataCopy = Object.assign({}, this.data);
        },
        tenantFilter(val, update, abort) {
            let filter = "";
            if (val) {
                filter = "name,like," + val + ";";
            }
            this.tenantRepository
                .getData(filter, 0, 20, "+name")
                .then(response => {
                    update(() => {
                        this.tenantOptions = response.data;
                    });
                })
                .catch(err => {
                    console.log(err);
                    abort();
                });
        },
        tenantInput(value) {
            if (!this.data.tenants) {
                this.data.tenants = [];
            }
            const exist =
                this.data.tenants
                    .map(tenant => {
                        return tenant.id;
                    })
                    .indexOf(value) >= 0
                    ? true
                    : false;
            if (!exist) {
                this.data.tenants.push(
                    this.tenantOptions.filter(tenant => {
                        return value == tenant.id;
                    })[0]
                );
            }
            this.selectedTenant = {};
        },
        removeTenant(index) {
            console.log("remove");
            this.data.tenants.splice(index, 1);
            this.rerender = !this.rerender;
        }
    }
};
</script>

