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

// 表格数据
const tableData = ref([
  // { name: 'Alice', age: 30, country: 'USA' ,gen:"xx"},
  // { name: 'Bob', age: 25, country: 'Canada' },
  // { name: 'Bob', age: 25, country: 'Canada' ,gen:"xx"},
]);
// 从 JSON 数据中提取列
const columns = ref([]);

const onClear = () => {
  SqlStatement.content=""
  tableData.value=[]
  columns.value=[]
}

const onSubmit = () => {
  // console.log('submit!')
  // console.log(SqlStatement.content)
  getTableData()
}


import {requestPack} from "../utils/requests.js";
const getTableData = async ()=>{
  // let res= await request.get(`user/list/?pageSize=${pageSize.value}&pageNum=${cur}`)
  let res= await requestPack.get(`/mydbms/index?statement=${SqlStatement.content}`)
  // console.log(res)
  console.log(tableData.value)
  tableData.value = res.msg
  // console.log(res.msg)
  console.log(tableData.value)
  if (tableData.value.length > 0) {
    columns.value = Object.keys(tableData.value[0]);
  }
}
// getTableData()


</script>

<template>

  <el-container >
    <el-header  style="padding: 0">
      <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
          @select="handleSelect"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
      >
        <el-menu-item index="1"><h1><strong>MiniDBMS</strong></h1></el-menu-item>
        <div class="flex-grow" />
<!--        <el-menu-item index="1">Processing Center</el-menu-item>-->
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
      </el-menu>
    </el-header>

    <el-container>
      <el-main style="padding: 50px; margin-left: 100px;margin-right: 100px" >
        <el-form :model="SqlStatement" label-width="120px" label-position="top">
          <el-form-item label="SQL 语句">
            <el-input v-model="SqlStatement.content" type="textarea" :rows="6"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交</el-button>
            <el-button @click="onClear">清空</el-button>
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

</template>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
</style>
