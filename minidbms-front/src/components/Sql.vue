<script lang="ts" setup>
import { ref,reactive,computed } from 'vue'

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

const onHelp = () => {
  SqlStatement.content="help"
  getTableData()
  SqlStatement.content=""
}

const onLook = () => {
  SqlStatement.content="show databases;"
  getTableData()
}

const DatabaseName = ref("")
const handleDbRowOp = (dbName) =>{
  // console.log(dbName)
  DatabaseName.value = dbName
  SqlStatement.content=`use database ${dbName};\nshow tables;`
  getTableData()
}

const tableName = ref("")
const handleTableRowOp = (tbName)=>{
  tableName.value = tbName
  SqlStatement.content=`select * from ${tbName};`
  getTableData()
}

const handleTableRowDel = (row) => {
  let key = "id"
  let id = row[key]
  // 完整的 SQL 删除语句
  SqlStatement.content = `DELETE FROM ${tableName.value} WHERE ${key}=${id};`
  // 调用函数以刷新数据
  getTableData();
  SqlStatement.content =""
}

const handleTableRowAdd = (row)=>{
  SqlStatement.content = `INSERT INTO ${tableName.value}(列名称1，列名称2，...)  values(列值1，列值2，...)`
}

const handleTableRowAlt = (row)=>{
  SqlStatement.content = `update ${tableName.value} set 列名称1=列值1，列名称2=列值2，... where 列名称=列值`
}

import {requestPack} from "../utils/requests.js";
import {Box, ChatDotRound, CloseBold, Coin, Files, House, Plus, Refrigerator, Tickets} from "@element-plus/icons-vue";
const getTableData = async ()=>{
  // let res= await request.get(`user/list/?pageSize=${pageSize.value}&pageNum=${cur}`)
  let res= await requestPack.get(`/mydbms/index?statement=${SqlStatement.content}`)
  console.log(res)
  tableData.value = res.msg
  console.log(res.msg)
  if (tableData.value.length > 0) {
    columns.value = Object.keys(tableData.value[0]);
  }
}
// getTableData()

SqlStatement.content="help"
getTableData()
SqlStatement.content=""

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
<!--        <el-sub-menu index="2" v-if="DatabaseName!==''">-->
<!--          <template #title>{{ DatabaseName }}</template>-->
<!--          <el-menu-item index="2-1" @click="handleTableRowOp(tableName)"-->
<!--          v-if="tableName!==''">-->
<!--            {{ tableName }}</el-menu-item>-->
<!--          <el-menu-item index="2-2">item two</el-menu-item>-->
<!--          <el-menu-item index="2-3">item three</el-menu-item>-->
<!--          <el-sub-menu index="2-4">-->
<!--            <template #title>item four</template>-->
<!--            <el-menu-item index="2-4-1">item one</el-menu-item>-->
<!--            <el-menu-item index="2-4-2">item two</el-menu-item>-->
<!--            <el-menu-item index="2-4-3">item three</el-menu-item>-->
<!--          </el-sub-menu>-->
<!--        </el-sub-menu>-->
      </el-menu>
    </el-header>

    <el-container>
      <el-main style="padding: 20px; margin-left: 20px;margin-right: 20px" >
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :md="14" :lg="16" :xl="16"
          >
            <el-button type="success" @click="onLook"><el-icon><Coin /></el-icon>数据库</el-button>
            <el-button @click="handleDbRowOp(DatabaseName)"
                       v-if="DatabaseName!==''">
              <el-icon><Files /></el-icon>{{ DatabaseName }}</el-button>
            <el-button  @click="handleTableRowOp(tableName)"
                        v-if="tableName!==''">
              <el-icon><Tickets /></el-icon>{{ tableName }}</el-button>
            <el-button type="success" @click="handleTableRowAdd"
                       v-if="SqlStatement.content.match('select') && tableName!==''"
            ><el-icon><Plus /></el-icon> 添加</el-button>

            <el-table stripe :data="tableData" max-height="500">
              <el-table-column
                  v-for="key in columns"
                  :prop="key"
                  :label="key"
                  sortable
              ></el-table-column>

              <el-table-column fixed="right" label="" width="60"
              v-if="SqlStatement.content==='show databases;'">
                <template #default="scope">
                  <el-button link type="primary" size="small"
                             @click="handleDbRowOp(scope.row.databaseName)"
                  >选择</el-button
                  >
                </template>
              </el-table-column>

              <el-table-column fixed="right" label="" width="60"
                               v-if="SqlStatement.content.match('show tables;') && DatabaseName!==''">
                <template #default="scope">
                  <el-button link type="primary" size="small"
                             @click="handleTableRowOp(scope.row.tableName)"
                  >查看</el-button
                  >
                </template>
              </el-table-column>

              <el-table-column fixed="right" label="" width="120"
                               v-if="SqlStatement.content.match('select') && tableName!==''">
                <template #default="scope">
                  <el-button link type="danger" size="small"
                             @click="handleTableRowDel(scope.row)"
                  >删除</el-button
                  >
                  <el-button link type="primary" size="small"
                             @click="handleTableRowAlt(scope.row)"
                  >修改</el-button
                  >
                </template>
              </el-table-column>
            </el-table>

          </el-col>

          <el-col :xs="24" :sm="24" :md="10" :lg="8" :xl="8"
          >

            <el-form :model="SqlStatement" label-width="120px" label-position="top">
              <el-form-item label="SQL 语句">
                <el-input v-model="SqlStatement.content" type="textarea" :rows="12"/>
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="onHelp"><el-icon><ChatDotRound /></el-icon>帮助</el-button>
                <div style="flex-grow: 1;"/>
                <el-button type="primary" @click="onSubmit"><el-icon><Select /> </el-icon> 提交</el-button>
                <el-button @click="onClear"><el-icon><CloseBold /></el-icon> 清空</el-button>
              </el-form-item>
            </el-form>

          </el-col>
        </el-row>




      </el-main>
      <el-footer style="padding: 0">
<!--        <el-row :gutter="20">-->
<!--          <el-col :span="16" class="foot-item"><div class="grid-content ep-bg-purple" ></div></el-col>-->
<!--          <el-col :span="8" class="foot-item"><div class="grid-content ep-bg-purple"></div></el-col>-->
<!--        </el-row>-->
        <el-row :gutter="20">
          <el-col :span="8" class="foot-item"><div class="grid-content ep-bg-purple" ></div></el-col>
          <el-col :span="8" class="foot-item"><div class="grid-content ep-bg-purple" >
            <p style="text-align: center; color: #888888"><strong>庄家宝 史海云 任辰宇 谈伽辉 林金锐</strong></p>
          </div></el-col>
          <el-col :span="4" class="foot-item"><div class="grid-content ep-bg-purple" ></div></el-col>
          <el-col :span="4" class="foot-item"><div class="grid-content ep-bg-purple" ></div></el-col>
        </el-row>
        <el-row :gutter="20" >
          <el-col :span="4" class="foot-bottom"><div class="grid-content ep-bg-purple" ></div></el-col>
          <el-col :span="16" class="foot-bottom"><div class="grid-content ep-bg-purple" >
            <p style="text-align: center; color: #888888"><strong>© 2024 Copyright: bytesc</strong></p>
          </div></el-col>
          <el-col :span="4" class="foot-bottom"><div class="grid-content ep-bg-purple" ></div></el-col>
        </el-row>
      </el-footer>
    </el-container>

  </el-container>

</template>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
.foot-item{
  padding: 0 !important;
  background: #dadada;
}
.foot-bottom{
  padding: 0 !important;
  background: #ebebeb;
}
.grid-content {
  min-height: 36px;
}

</style>
