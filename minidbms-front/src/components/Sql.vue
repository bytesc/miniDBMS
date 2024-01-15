<script lang="ts" setup>
import { ref,reactive } from 'vue'

defineProps({
  msg: String,
})

const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const SqlStatement = reactive({
  content: '',
})

const onSubmit = () => {
  console.log('submit!')
}


const jsonData = [
  { name: 'Alice', age: 30, country: 'USA' ,gen:"xx"},
  { name: 'Bob', age: 25, country: 'Canada' },
  { name: 'Bob', age: 25, country: 'Canada' ,gen:"xx"},
];

// 从 JSON 数据中提取列
const columns = ref([]);
if (jsonData.length > 0) {
  columns.value = Object.keys(jsonData[0]);
}

// 表格数据
const tableData = ref(jsonData);

</script>

<template>
<!--  <h1>{{ msg }}</h1>-->

<!--  <div class="card">-->
<!--    <button type="button" @click="count++">count is {{ count }}</button>-->
<!--    <p>-->
<!--      Edit-->
<!--      <code>components/Sql.vue</code> to test HMR-->
<!--    </p>-->
<!--  </div>-->

<!--  <p>-->
<!--    Check out-->
<!--    <a href="https://vuejs.org/guide/quick-start.html#local" target="_blank"-->
<!--      >create-vue</a-->
<!--    >, the official Vue + Vite starter-->
<!--  </p>-->
<!--  <p>-->
<!--    Install-->
<!--    <a href="https://github.com/vuejs/language-tools" target="_blank">Volar</a>-->
<!--    in your IDE for a better DX-->
<!--  </p>-->
<!--  <p class="read-the-docs">Click on the Vite and Vue logos to learn more</p>-->
<div class="common-layout">
  <el-container>
    <el-header style="padding: 0">
      <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
      >
        <el-menu-item index="1">Processing Center</el-menu-item>
        <el-sub-menu index="2">
          <template #title>Workspace</template>
          <el-menu-item index="2-1">item one</el-menu-item>
          <el-menu-item index="2-2">item two</el-menu-item>
          <el-menu-item index="2-3">item three</el-menu-item>
          <el-sub-menu index="2-4">
            <template #title>item four</template>
            <el-menu-item index="2-4-1">item one</el-menu-item>
            <el-menu-item index="2-4-2">item two</el-menu-item>
            <el-menu-item index="2-4-3">item three</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-menu-item index="3" disabled>Info</el-menu-item>
        <el-menu-item index="4">Orders</el-menu-item>
      </el-menu>
    </el-header>
    <el-container>
      <el-main style="padding: 50px">
        <el-form :model="SqlStatement" label-width="120px">
          <el-form-item label="Sql statement">
            <el-input v-model="SqlStatement.content" type="textarea" :rows="6"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">Create</el-button>
            <el-button>Cancel</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="tableData">
          <el-table-column
              v-for="key in columns"
              :key="key"
              :prop="key"
              :label="key"
          ></el-table-column>
        </el-table>

      </el-main>
    </el-container>

  </el-container>
</div>
</template>

<style scoped>
/*.read-the-docs {*/
/*  color: #888;*/
/*}*/
</style>
