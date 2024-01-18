<script lang="ts" setup>
import { ref,reactive,computed } from 'vue'

defineProps({
  msg: String,
})

const addDialogVisible = ref(false)
const altDialogVisible = ref(false)

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

const SubmitData = ref({
})

const CurDatabaseName = ref("")
const CurTableName = ref("")


const onClear = () => {
  SqlStatement.content=""
  tableData.value=[]
  columns.value=[]
  SubmitData.value={}
  CurTableName.value=""
  CurDatabaseName.value=""
}

const onSubmit = () => {
  // console.log('submit!')
  // console.log(SqlStatement.content)
  CurTableName.value=""
  CurDatabaseName.value=""
  getTableData()
}

const onHelp = () => {
  onClear()
  SqlStatement.content="help"
  getTableData()
  SqlStatement.content=""
}

const onLook = () => {
  CurDatabaseName.value = ""
  CurTableName.value = ""
  SqlStatement.content="show databases;"
  getTableData()
}


const handleDbRowOp = (dbName) =>{
  // console.log(dbName)
  CurDatabaseName.value = dbName
  CurTableName.value = ""
  SqlStatement.content=`use database ${dbName};\nshow tables;`
  getTableData()
}


const handleTableRowOp = (tbName)=>{
  CurTableName.value = tbName
  SqlStatement.content=`select * from ${tbName};`
  getTableData()
}

const handleTableRowDel = (row) => {
  let key = "id"
  let id = row[key]
  // 完整的 SQL 删除语句
  SqlStatement.content = `DELETE FROM ${CurTableName.value} WHERE ${key}=${id};`
  // 调用函数以刷新数据
  getTableData();
  SqlStatement.content =""
}

const handleTableRowAdd = (row)=>{
  // SqlStatement.content = `INSERT INTO ${tableName.value}(列名称1，列名称2，...)  values(列值1，列值2，...)`
  addDialogVisible.value=true
  SubmitData.value = Object.keys(tableData.value[0]).reduce((acc, key) => {
    acc[key] = ''; // 将每个键的值设置为空字符串
    return acc;
  }, {});
}
const handleTableRowAddCommit = ()=>{
  const buildInsertSql = (tableName, rowData) => {
    const columns = Object.keys(rowData).join(', ');
    const values = Object.keys(rowData).map(key => {
      const value = rowData[key];
      return typeof value === 'string' ? `${value}` : value;
    }).join(', ');

    return `INSERT INTO ${tableName}(${columns}) VALUES(${values});`;
  };

  SqlStatement.content = buildInsertSql(CurTableName.value, SubmitData.value);
  getTableData();
  addDialogVisible.value = false
}

const handleTableRowAlt = (row)=>{
  // SqlStatement.content = `update ${tableName.value} set 列名称1=列值1，列名称2=列值2，... where 列名称=列值`
  altDialogVisible.value=true
  SubmitData.value = { ...row };

}
const handleTableRowAltCommit = ()=>{
  const buildUpdateSql = (tableName, rowData, key) => {
    const keyValue = rowData[key];
    const setClause = Object.keys(rowData).filter(k => k !== key)
        .map(k => {
          const value = rowData[k];
          return `${k}=${typeof value === 'string' ? `${value}` : value}`;
        }).join(',');

    return `UPDATE ${tableName} SET ${setClause} WHERE ${key}=${keyValue};`;
  };

  SqlStatement.content = buildUpdateSql(CurTableName.value, SubmitData.value, 'id');
  getTableData();
  altDialogVisible.value = false
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
      </el-menu>
    </el-header>

    <el-container>
      <el-main style="padding: 20px; margin-left: 20px;margin-right: 20px" >
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :md="14" :lg="16" :xl="16"
          >
            <el-button type="success" @click="onLook"><el-icon><Coin /></el-icon>数据库</el-button>
            <el-button @click="handleDbRowOp(CurDatabaseName)"
                       v-if="CurDatabaseName!==''">
              <el-icon><Files /></el-icon>{{ CurDatabaseName }}</el-button>
            <el-button  @click="handleTableRowOp(CurTableName)"
                        v-if="CurTableName!==''">
              <el-icon><Tickets /></el-icon>{{ CurTableName }}</el-button>
            <el-button type="success" @click="handleTableRowAdd"
                       v-if="SqlStatement.content.match('select') && CurTableName!==''"
            ><el-icon><Plus /></el-icon> 添加</el-button>

            <el-table stripe :data="tableData" max-height="400">
              <el-table-column fixed="left" label="" width="60"
                               v-if="SqlStatement.content==='show databases;'">
                <template #default="scope">
                  <el-button link type="primary" size="small"
                             @click="handleDbRowOp(scope.row.databaseName)"
                  >选择</el-button
                  >
                </template>
              </el-table-column>

              <el-table-column fixed="left" label="" width="60"
                               v-if="SqlStatement.content.match('show tables;') && CurDatabaseName!==''">
                <template #default="scope">
                  <el-button link type="primary" size="small"
                             @click="handleTableRowOp(scope.row.tableName)"
                  >查看</el-button
                  >
                </template>
              </el-table-column>

              <el-table-column
                  v-for="key in columns"
                  :prop="key"
                  :label="key"
                  sortable
              ></el-table-column>

              <el-table-column fixed="right" label="" width="120"
                               v-if="SqlStatement.content.match('select') && CurTableName!==''">
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
        <el-row :gutter="20" >
          <el-col :span="4" class="foot-bottom"><div class="grid-content ep-bg-purple" ></div></el-col>
          <el-col :span="16" class="foot-bottom"><div class="grid-content ep-bg-purple" >
            <a href="http://www.bytesc.top" >
<!--              style="text-decoration: none;-->
              <p style="text-align: center; color: #888888"><strong>© 2024 Copyright: bytesc</strong></p>
            </a>
          </div></el-col>
          <el-col :span="4" class="foot-bottom"><div class="grid-content ep-bg-purple" ></div></el-col>
        </el-row>
      </el-footer>
    </el-container>

  </el-container>


  <el-dialog
      v-model="addDialogVisible"
      title="添加数据"
      width="30%"
      align-center
  >
      <el-form label-width="100px" :model="SubmitData" label-position="left" style="max-width: 600px">
        <el-form-item v-for="(value, key) in SubmitData" :key="key" :label="key">
          <el-input v-model="SubmitData[key]" />
        </el-form-item>
      </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTableRowAddCommit">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog
      v-model="altDialogVisible"
      title="修改数据"
      width="30%"
      align-center
  >
    <el-form label-width="100px" :model="SubmitData" label-position="left" style="max-width: 600px">
      <el-form-item v-for="(value, key) in SubmitData" :key="key" :label="key">
        <el-input v-model="SubmitData[key]" :disabled="key === 'id'"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="altDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTableRowAltCommit">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
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
