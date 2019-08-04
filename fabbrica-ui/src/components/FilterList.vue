<template>
  <q-card>
    <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);" class="bg-grey-2">
      <div class="col-1 q-py-xs q-px-md text-right">Filters</div>
      <q-chip
        v-for="(filter, index) in filters"
        :key="index"
        square
        outline
        removable
        color="primary"
        label="{filter.columnLabel + ' ' + filter.condition + ' ' + filter.value}"
        class="q-ma-none q-px-sm q-py-none bg-white q-mr-sm"
      />
      <div v-if="!filterInputVisible">
        <q-btn
          outline
          color="primary"
          class="bg-white q-px-sm"
          icon="add"
          label="Add Filter"
          size="sm"
        />
      </div>
    </q-toolbar>
    <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);" class="bg-grey-2">
      <div class="col-1 q-py-sm q-px-md text-right">Sorts</div>
      <div v-if="!sortInputVisible">
        <q-btn
          outline
          color="primary"
          class="bg-white q-px-sm"
          icon="add"
          label="Add Sort"
          size="sm"
        />
      </div>
    </q-toolbar>
    <q-markup-table>
      <thead>
        <tr>
          <th
            class="text-left bg-blue-grey-2"
            v-for="column in columns"
            :key="column.name"
          >{{ column.label}}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row,index) in data" :key="index" @click="rowClick(row.id)">
          <td
            class="text-left"
            v-for="column in columns"
            :key="column.name"
          >{{ $_.get(row,column.name) }}</td>
        </tr>
      </tbody>
    </q-markup-table>
    <q-toolbar style="border-top: 1px solid rgba(0,0,0,0.12);" class="bg-grey-2">
      <q-pagination
        v-model="currentPage"
        color="light-blue-6"
        :max="totalRows/pageSize > 0 ? totalRows/pageSize : 1"
        :max-pages="7"
        :direction-links="true"
        size="12px"
        @input="pageChanged"
      />
      <q-space />
      Total {{ totalRows }}
      <q-select
        outlined
        color="primary"
        v-model="pageSize"
        :options="[1, 10, 20, 50, 100]"
        suffix="per Page"
        size="sm"
        class="q-ml-md"
      />
    </q-toolbar>
  </q-card>
</template>

<script>
export const FilterConditions = {
    Equals: "=",
    NotEquals: "!="
};

export default {
    props: ["repository", "columns", "rowClick"],
    data() {
        return {
            data: [],
            filters: [],
            sorts: [],
            filterInputVisible: false,
            sortInputVisible: false,
            newFilter: {
                columnName: "",
                columnLabel: "",
                condition: FilterConditions.Equals,
                values: ""
            },
            newSort: {
                columnName: "",
                columnLabel: "",
                ascending: true
            },
            totalRows: 0,
            currentPage: 1,
            pageSize: 20
        };
    },
    mounted() {
        this.updateData();
    },
    methods: {
        updateData() {
            let sort = "";
            this.sorts.forEach(sort => {
                sort = sort + (sort.ascending ? "+" : "-") + sort.columnName;
            });
            let filterString = "";
            this.filters.forEach(filter => {
                filterString =
                    filterString +
                    filter.columnName +
                    "," +
                    filter.condition +
                    "," +
                    filter.value +
                    ";";
            });
            if (this.repository != null) {
                this.repository
                    .getData(
                        filterString,
                        this.currentPage - 1,
                        this.pageSize,
                        sort
                    )
                    .then(response => {
                        this.loadData(
                            response.data,
                            parseInt(response.headers["x-total-count"])
                        );
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        },
        loadData(newData, newTotal) {
            this.data = newData;
            this.totalRows = newTotal;
        },
        pageChanged() {
            this.updateData();
        }
    }
};
</script>

<style lang="stylus" scoped>
th {
  font-size: 16px;
}

.q-toolbar {
  min-height: 40px;
}

.q-btn {
  border-radius: 4px;
}

.q-btn >>> .q-icon {
  margin-right: 4px;
}

.q-chip {
  height: 28px;
  font-size: 12px;
}

.q-select {
  min-height: 28px;
}

.q-select >>> .q-field__control {
  min-height: 28px;
}

.q-select >>> .q-field__native {
  min-height: 28px;
}

.q-select >>> .q-field__append {
  min-height: 28px;
  height: 28px;
}
</style>


