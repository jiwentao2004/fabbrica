<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el label="Factories" icon="domain" :to="{ name: 'factories' }" />
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
                v-model="data.code"
                label="Factory Code"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input
                outlined
                v-model="data.name"
                label="Factory Name"
                class="q-mb-sm"
                :disable="!edit"
              />
            </div>
            <div class="col-md-4 col-sm-6">
              <q-select
                outlined
                clearable
                v-model="data.tenant"
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
            </div>
          </div>
        </q-card>
      </div>
    </q-form>
  </q-page>
</template>

<script>
import Repository from "../../repository";
export default {
    data() {
        return {
            data: {
                name: "",
                code: "",
                tenant: {}
            },
            tenantOptions: [],
            dataCopy: undefined,
            edit: false,
            loading: false,
            repository: new Repository("factories", this.$http),
            tenantRepository: new Repository("tenants", this.$http)
        };
    },
    beforeMount() {
        if (this.$route.params.id != "new") {
            this.repository
                .get(this.$route.params.id)
                .then(response => {
                    this.data = response.data;
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
            this.data.tenant = this.tenantOptions.filter(tenant => {
                return value == tenant.id;
            })[0];
        }
    }
};
</script>

