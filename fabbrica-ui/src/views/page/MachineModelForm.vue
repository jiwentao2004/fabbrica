<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el
            label="Machine Models"
            icon="location_city"
            :to="{ name: 'machine-models' }"
          />
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
                label="Machine Model Code"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input
                outlined
                v-model="data.name"
                label="Machine Model Name"
                class="q-mb-sm"
                :disable="!edit"
              />
            </div>
            <div class="col-md-4 col-sm-6">
              <q-input
                outlined
                v-model="measurement"
                label="Measurement"
                class="q-mb-sm"
                v-show="edit"
                :disable="!edit"
              >
                <template v-slot:append>
                  <q-btn flat outlined icon="add" @click="addMeasurement" class="cursor-pointer" />
                </template>
              </q-input>
              <q-slider
                v-model="frequency"
                :label-value="frequency + ' ms'"
                :min="0"
                :max="10000"
                :step="10"
                snap
                label
                label-always
                v-show="edit"
                color="light-blue-6"
              />
              <q-list
                bordered
                separator
                v-if="data.measurements && data.measurements.length > 0"
                class="rounded-borders"
                v-model="rerender"
              >
                <q-item
                  v-for="(measurement, index) in data.measurements"
                  :key="index"
                  clickable
                  v-ripple
                >
                  <q-item-section avatar>
                    <q-avatar color="primary" text-color="white">{{ measurement.name.charAt(0) }}</q-avatar>
                  </q-item-section>

                  <q-item-section>
                    <q-item-label>{{ measurement.name }}</q-item-label>
                    <q-item-label caption lines="1">{{ measurement.frequency }}</q-item-label>
                  </q-item-section>

                  <q-item-section side>
                    <q-btn
                      flat
                      outlined
                      icon="close"
                      color="red-6"
                      @click="removeMeasurement(index)"
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
import Repository from "../../repository";
export default {
    data() {
        return {
            data: {
                name: "",
                code: ""
            },
            dataCopy: undefined,
            edit: false,
            loading: false,
            measurement: "",
            frequency: 1000,
            rerender: false,
            repository: new Repository("machineModels", this.$http)
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
        addMeasurement() {
            if (!this.data.measurements) {
                this.data.measurements = [];
            }
            const exist =
                this.data.measurements
                    .map(measurement => {
                        return measurement.name;
                    })
                    .indexOf(this.measurement) >= 0
                    ? true
                    : false;
            if (!exist) {
                this.data.measurements.push({
                    name: this.measurement,
                    frequency: this.frequency
                });
            }
            this.measurement = "";
            this.frequency = 1000;
        },
        removeMeasurement(index) {
            console.log("remove");
            this.data.measurements.splice(index, 1);
            this.rerender = !this.rerender;
        }
    }
};
</script>

