import Vue from 'vue'

import './styles/quasar.styl'
import 'quasar/dist/quasar.ie.polyfills'
import '@quasar/extras/roboto-font/roboto-font.css'
import '@quasar/extras/material-icons/material-icons.css'
import {
  Quasar,
  QLayout,
  QHeader,
  QDrawer,
  QPageContainer,
  QPage,
  QToolbar,
  QToolbarTitle,
  QBtn,
  QIcon,
  QList,
  QItem,
  QItemSection,
  QItemLabel,
  QAvatar,
  QScrollArea,
  QCard,
  QCardSection,
  QCardActions,
  QSeparator,
  QForm,
  QInput,
  QSpace,
  QChip,
  QMenu,
  QBreadcrumbs,
  QBreadcrumbsEl,
  QMarkupTable,
  QPagination,
  QSelect,
  Ripple,
  ClosePopup,
  Loading
} from 'quasar'

Vue.use(Quasar, {
  config: {},
  components: {
    QLayout,
    QHeader,
    QDrawer,
    QPageContainer,
    QPage,
    QToolbar,
    QToolbarTitle,
    QBtn,
    QIcon,
    QList,
    QItem,
    QItemSection,
    QItemLabel,
    QAvatar,
    QScrollArea,
    QCard,
    QCardSection,
    QCardActions,
    QSeparator,
    QForm,
    QInput,
    QSpace,
    QChip,
    QMenu,
    QBreadcrumbs,
    QBreadcrumbsEl,
    QMarkupTable,
    QPagination,
    QSelect
  },
  directives: {
    Ripple,
    ClosePopup
  },
  plugins: {
    Loading
  }
})