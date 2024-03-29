<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el label="Machines" icon="location_city" :to="{ name: 'machines' }" />
          <q-breadcrumbs-el label="New" icon="add" />
        </q-breadcrumbs>
        <q-space />
        <q-btn v-if="edit" unelevated color="light-blue-6 q-mr-sm" @click="save">Save</q-btn>
        <q-btn v-if="!edit" unelevated color="light-blue-6 q-mr-sm" @click="enableEdit">Edit</q-btn>
        <q-btn v-if="!edit" unelevated color="red-6" @click="remove">Delete</q-btn>
        <q-btn v-if="edit" unelevated color="blue-grey-6" @click="cancel">Cancel</q-btn>
      </q-toolbar>
      <div class="row q-pa-md flex">
        <q-card class="full-width q-pa-md">
          <div class="row q-gutter-md">
            <div class="col-md-4 col-sm-6">
              <q-input
                outlined
                v-model="data.code"
                label="Machine Code"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input
                outlined
                v-model="data.name"
                label="Machine Name"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input
                outlined
                v-if="data.clientid"
                v-model="data.clientid"
                label="Client Id"
                class="q-mb-sm"
                disable
              />
            </div>
            <div class="col-md-4 col-sm-6">
              <q-select
                outlined
                clearable
                v-model="data.factory"
                use-input
                hide-selected
                fill-input
                input-debounce="0"
                label="Factory"
                :options="factoryOptions"
                option-label="name"
                option-value="id"
                emit-value
                map-options
                @filter="factoryFilter"
                @input="factoryInput"
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
                    <q-item-section class="text-grey">No Factories</q-item-section>
                  </q-item>
                </template>
              </q-select>
              <q-select
                outlined
                clearable
                v-model="data.machineModel"
                use-input
                hide-selected
                fill-input
                input-debounce="0"
                label="Machine Model"
                :options="machineModelOptions"
                option-label="name"
                option-value="id"
                emit-value
                map-options
                @filter="machineModelFilter"
                @input="machineModelInput"
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
                    <q-item-section class="text-grey">No Machine Models</q-item-section>
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
                code: ""
            },
            factoryOptions: [],
            machineModelOptions: [],
            dataCopy: undefined,
            edit: false,
            loading: false,
            repository: new Repository("machines", this.$http),
            factoryRepository: new Repository("factories", this.$http),
            machineModelRepository: new Repository("machineModels", this.$http)
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
        remove() {
            this.$q.loading.show();
            this.repository
                .remove(this.data.id)
                .then(response => {
                    if (response.status == 204) {
                        this.$q.loading.hide();
                        this.$router.go(-1);
                    }
                })
                .catch(err => {
                    this.$q.loading.hide();
                    console.log(err);
                });
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
                    this.$q.loading.hide();
                    console.log(err);
                });
        },
        enableEdit() {
            this.edit = true;
            this.dataCopy = Object.assign({}, this.data);
        },
        factoryFilter(val, update, abort) {
            let filter = "";
            if (val) {
                filter = "name,like," + val + ";";
            }
            this.factoryRepository
                .getData(filter, 0, 20, "+name")
                .then(response => {
                    update(() => {
                        this.factoryOptions = response.data;
                    });
                })
                .catch(err => {
                    console.log(err);
                    abort();
                });
        },
        factoryInput(value) {
            this.data.factory = this.factoryOptions.filter(factory => {
                return value == factory.id;
            })[0];
        },
        machineModelFilter(val, update, abort) {
            let filter = "";
            if (val) {
                filter = "name,like," + val + ";";
            }
            this.machineModelRepository
                .getData(filter, 0, 20, "+name")
                .then(response => {
                    update(() => {
                        this.machineModelOptions = response.data;
                    });
                })
                .catch(err => {
                    console.log(err);
                    abort();
                });
        },
        machineModelInput(value) {
            this.data.machineModel = this.machineModelOptions.filter(
                machineModel => {
                    return value == machineModel.id;
                }
            )[0];
        }
    }
};
</script>

