<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.regionAddress" clearable class="filter-item" style="width: 200px;" placeholder="请输入区域位置" />
      <el-button
        v-permission="['GET admin/shipping/search']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找
      </el-button>
      <el-button
        v-permission="['POST admin/shipping/create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加
      </el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中..." border fit highlight-current-row>

      <el-table-column align="center" label="运费ID" prop="id" sortable />
      <el-table-column align="center" label="满减最低消费" prop="expressFreightMin" />
      <el-table-column align="center" label="不满所需运费" prop="freightValue" />
      <el-table-column align="center" label="区域位置" prop="regionAddress" />
      <el-table-column align="center" label="是否启用" prop="enabled">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'error' ">{{ scope.row.enabled ? '启用' : '不启用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['POST /admin/shipping/update']"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑
          </el-button>
          <el-button
            v-permission="['GET /admin/shipping/delete']"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getShippingList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:30px;"
      >
        <el-form-item label="满减最低消费" prop="expressFreightMin">
          <el-input v-model="dataForm.expressFreightMin" placeholder="最大999元" />
        </el-form-item>
        <el-form-item label="不满所需运费" prop="freightValue">
          <el-input v-model="dataForm.freightValue" placeholder="最大999元" />
        </el-form-item>
        <el-form-item label="区域位置" prop="regionAddress">
          <el-autocomplete
            v-model="dataForm.regionAddress"
            popper-class="my-autocomplete"
            placeholder="区域位置"
            :fetch-suggestions="queryRegion"
            :trigger-on-focus="false"
            @select="handleSelectRegion"
          >

            <template slot-scope="{ item }" class="inlineBlock">
              <div class="name">{{ item.name }}</div>
              <span class="addr" style="font-size: 12px;color: #b4b4b4;">{{ item.address }}</span>
            </template>
          </el-autocomplete>
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="dataForm.enabled" placeholder="请选择">
            <el-option :value="true" label="启用" />
            <el-option :value="false" label="不启用" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createShipping">确定</el-button>
        <el-button v-else type="primary" @click="updateShipping">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<style>
  .my-autocomplete li {
    line-height: normal;
    padding: 7px;
  }
</style>
<script>
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { shippingList, shippingSearch, shippingCreate, shippingUpdate, shippingDelete } from '@/api/shipping'
import { searchListRegion } from '@/api/region'

export default {
  name: 'ConfigExpress',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        regionAddress: undefined
      },
      dataForm: {
        id: undefined,
        expressFreightMin: undefined,
        freightValue: undefined,
        regionId: undefined,
        regionCode: undefined,
        regionAddress: undefined,
        position: 1,
        enabled: true
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        expressFreightMin: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        freightValue: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        regionAddress: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  created() {
    this.getShippingList()
  },
  methods: {
    getShippingList() {
      console.log(this.listQuery)
      this.listLoading = true
      shippingList(this.listQuery).then(response => {
        this.list = response.data.data.list
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    queryRegion(queryString, cb) {
      if (queryString != null) {
        searchListRegion({ name: this.dataForm.regionAddress })
          .then(res => {
            // type == 1 省  2 市 3 县区
            var data = res.data.data.list
            for (const ex of data) {
              ex.value = ex.name // ps:必须为每个对象增加value字段！！因为autocomplete只识别value字段并在下拉列中显示
            }
            cb(data)
          }).catch(res => {
            this.$notify.error({
              title: '查询失败',
              message: res.data
            })
          })
      } else {
        this.$notify.error({
          title: '填写文本',
          message: '查询出错'
        })
      }
    },
    handleSelectRegion(item) {
      this.dataForm.regionId = item.id
      this.dataForm.regionAddress = item.address
      this.dataForm.regionCode = item.code
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        expressFreightMin: undefined,
        freightValue: undefined,
        regionId: undefined,
        regionAddress: undefined,
        regionCode: undefined,
        position: 1,
        enabled: true
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getShippingList()
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createShipping() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          const dataForm = this.dataForm
          console.log(dataForm)
          shippingCreate(dataForm)
            .then((res) => {
              this.list.unshift(res.data.data)
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    updateShipping() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          shippingUpdate(this.dataForm)
            .then(() => {
              for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    handleDelete(row) {
      console.log(row.id)
      shippingDelete({ id: row.id })
        .then(res => {
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
        .catch(res => {
          this.$notify.error({
            title: '失败',
            message: res.data.errmsg
          })
        })
    }
  }
}
</script>

