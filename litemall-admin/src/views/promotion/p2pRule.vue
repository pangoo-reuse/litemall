<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.goodsName" clearable class="filter-item" style="width: 200px;" placeholder="请输入商品名" />
      <el-button
        v-permission="['GET /admin/p2p/list']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找
      </el-button>
      <el-button
        v-permission="['POST /admin/p2p/create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加
      </el-button>
      <el-button
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >导出
      </el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="团购ID" prop="id" />

      <el-table-column align="center" label="商品ID" prop="goodsId" />

      <el-table-column align="center" min-width="100" label="名称" prop="goodsName" />

      <el-table-column align="center" property="picUrl" label="图片">
        <template slot-scope="scope">
          <img :src="scope.row.picUrl" width="40">
        </template>
      </el-table-column>

      <el-table-column align="center" label="最大限制人数" prop="maxPersonCount" />

      <el-table-column align="center" label="团购商品基数" prop="goodsCount" />

      <el-table-column align="center" label="状态" prop="state">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state === 0 ? 'success' : 'error' ">{{ scope.row.state ? '启用' : '不启用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="产品规则" prop="productRule">
        <template slot-scope="scope">
          <el-tag :type="'success'">{{ statusMap[scope.row.productRule] == 0 ? '固定总份数':'固定总人数' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发货规则" prop="shippingRule">
        <template slot-scope="scope">
          <el-tag :type="'success'">{{ statusMap[scope.row.shippingRule] == 0 ? '退差价':'补份数' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间" prop="createdTime" />
      <el-table-column align="center" label="结束时间" prop="expireTime" />

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['POST /admin/p2p/update']"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑
          </el-button>
          <el-button
            v-permission="['POST /admin/p2p/delete']"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="商品ID" prop="goodsId">
          <el-input v-model="dataForm.goodsId" :disabled="isEditable" />
        </el-form-item>
        <el-form-item label="最大限制人数" prop="maxPersonCount">
          <el-input v-model="dataForm.maxPersonCount" />
        </el-form-item>
        <el-form-item label="团购商品基数" prop="goodsCount">
          <el-input v-model="dataForm.goodsCount" />
        </el-form-item>

        <el-form-item label="产品规则" prop="productRule">
          <el-select v-model="dataForm.productRule" placeholder="请选择">
            <el-option :value="0" label="固定总份数" />
            <el-option :value="1" label="固定总人数" />
          </el-select>
        </el-form-item>
        <el-form-item label="发货规则" prop="shippingRule">
          <el-select v-model="dataForm.shippingRule" placeholder="请选择">
            <el-option :value="0" label="退差价" />
            <el-option :value="1" label="补份数" />
          </el-select>
        </el-form-item>

        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker
            v-model="dataForm.expireTime"
            type="datetime"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="dataForm.state" placeholder="请选择">
            <el-option :value="true" label="启用" />
            <el-option :value="false" label="不启用" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createRule">确定</el-button>
        <el-button v-else type="primary" @click="updateRule">确定</el-button>
      </div>
    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

  </div>
</template>

<script>
import { editRule, listRules, deleteRule, createRule } from '@/api/p2p'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'P2pRule',
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        goodsName: undefined,
        sort: 'created_time',
        order: 'desc'
      },
      downloadLoading: false,
      dataForm: {
        id: undefined,
        goodsId: '',
        maxPersonCount: 0,
        goodsCount: 0,
        productRule: 0,
        shippingRule: 0,
        state: true,
        expireTime: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      isEditable: false,
      textMap: {
        update: '编辑',
        create: '创建'
      },
      statusMap: [
        '正常',
        '到期下线',
        '提前下线'
      ],
      rules: {
        goodsId: [{ required: true, message: '商品不能为空', trigger: 'blur' }],
        maxPersonCount: [{ required: true, message: '最大限制人数大于0', trigger: 'blur' }],
        goodsCount: [{ required: true, message: '团购商品基数大于0', trigger: 'blur' }],
        expireTime: [{ required: true, message: '过期时间不能为空', trigger: 'blur' }],
        shippingRule: [{ required: true, message: '发货规则必选', trigger: 'blur' }],
        productRule: [{ required: true, message: '产品规则必选', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listRules(this.listQuery).then(response => {
        this.list = response.data.data.list
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        goodsId: '',
        discount: '',
        discountMember: '',
        expireTime: undefined
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.isEditable = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createRule() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createRule(this.dataForm).then(response => {
            this.list.unshift(response.data.data)
            this.dialogFormVisible = false
            this.$notify.success({
              title: '成功',
              message: '创建团购规则成功'
            })
          }).catch(response => {
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
      this.isEditable = true
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateRule() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          editRule(this.dataForm).then(() => {
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
              message: '更新团购规则成功'
            })
          }).catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
        }
      })
    },
    handleDelete(row) {
      deleteRule(row).then(response => {
        this.$notify.success({
          title: '成功',
          message: '删除团购规则成功'
        })
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
      }).catch(response => {
        this.$notify.error({
          title: '失败',
          message: response.data.errmsg
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                  const tHeader = ['商品ID', '名称', '首页主图', '折扣', '人数要求', '活动开始时间', '活动结束时间']
                  const filterVal = ['id', 'name', 'pic_url', 'discount', 'discountMember', 'addTime', 'expireTime']
                  excel.export_json_to_excel2(tHeader, this.list, filterVal, '商品信息')
                  this.downloadLoading = false
                })
    }
  }
}
</script>
