<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="query.goodsName" clearable class="filter-item" style="width: 200px;" placeholder="活动产品标题" />
      <el-button
        v-permission="['GET /admin/p2p/list']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="getList"
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
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中..." border fit highlight-current-row>

      <el-table-column align="center" label="产品ID" prop="id" sortable />
      <el-table-column align="center" label="产品名" prop="goodsName" />
      <el-table-column align="center" label="产品主图" prop="picUrl">
        <template slot-scope="scope">
          <img :src="scope.row.picUrl" width="40">
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createdTime" />
      <el-table-column align="center" label="下线时间" prop="expireTime" />

      <el-table-column align="center" label="启用状态" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status ? 'success' : 'error' ">{{ scope.row.status ? '启用' : '不启用' }}</el-tag>
        </template>
      </el-table-column>
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
            v-permission="['GET /admin/p2p/delete']"
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
      <el-form ref="dataForm" :model="dataForm" :rules="rules">
        <el-form-item label="商品名" prop="goodsName">
          <el-autocomplete
            v-model="dataForm.goodsName"
            :aria-disabled="true"
            popper-class="m-autocomplete"
            placeholder="商品名或关键字"
            :fetch-suggestions="queryGoods"
            :trigger-on-focus="false"
            :disabled="dialogStatus==='update'"
            @select="handleGoodsSelected"
          >
            <template slot-scope="{ item }" class="inlineBlock">
              <div class="name">{{ item.name }}</div>
              <span style="font-size: 12px;color: #b4b4b4;">{{ item.brief }}</span>
            </template>
          </el-autocomplete>
        </el-form-item>

        <el-form-item label="下线时间" prop="expireTime">
          <el-date-picker
            v-model="dataForm.expireTime"
            type="datetime"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-select v-model="dataForm.status" placeholder="请选择">
            <el-option :value="true" label="启用" />
            <el-option :value="false" label="不启用" />
          </el-select>
        </el-form-item>

        <el-table :data="dataForm.products" stripe border style="width: 100%" size="mini">
          <el-table-column label="图片" align="center" valign="center" width="90">
            <template slot-scope="scope">
              <el-form-item prop="url" style="margin-bottom: 0 !important; margin-top: 10px !important;">
                <img :src="scope.row.url" height="50" style="background-color: #ececec">
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="商品ID" align="center" valign="center" prop="goodsId" />
          <el-table-column label="属性" align="center" valign="center" prop="specifications" />
          <el-table-column
            :label="dataForm.unit == undefined ? '数量':'数量/'+dataForm.unit "
            align="center"
            valign="center"
          >
            <template slot-scope="scope">
              <el-form-item prop="number" style="margin-bottom: 0 !important; margin-top: 8px !important;">
                <!--                ≥原始价-->
                <el-input v-model="scope.row.number" size="small" placeholder="≥原售价" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            :label="dataForm.unit == undefined ? '最低售价(元)':'最低售价(元)/'+dataForm.unit "
            align="center"
            valign="center"
            width="110"
          >
            <template slot-scope="scope">
              <el-form-item prop="price" style="margin-bottom: 0 !important; margin-top: 8px !important;">
                <el-input v-model="scope.row.price" size="small" placeholder="成本+利润" />

              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="规则" align="center" valign="center">
            <template slot-scope="scope">
              <el-form-item prop="rule" style="margin-bottom: 0 !important; margin-top: 8px !important;">
                <el-select v-model="scope.row.rule" placeholder="请选择">
                  <el-option :value="0" label="退差价" />
                  <el-option :value="1" label="补数量" />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="是否启用" align="center" valign="center">
            <template slot-scope="scope">
              <el-form-item prop="enabled" style="margin-bottom: 0 !important; margin-top: 8px !important;">
                <el-checkbox v-model="scope.row.enabled" :label="true">启用</el-checkbox>
              </el-form-item>
            </template>
          </el-table-column>

        </el-table>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<style scoped>
  .filter-item {
    margin-left: 0px;
  }

  .el-autocomplete {
    width: 80%;
  }

  .m-autocomplete li {
    line-height: normal;
    padding: 7px;

  }

  .el-table td, .el-table th {
    text-align: center !important;
  }

</style>

<script>
import { listCategory, listCatL1, createCategory, updateCategory, deleteCategory } from '@/api/category'
import { getToken } from '@/utils/auth'
import { listRules, createRule, deleteRule, editRule, queryGoods, queryGoodsProduct } from '@/api/p2p'

export default {
  name: 'P2P',
  data() {
    return {
      list: [],
      listLoading: true,
      catL1: {},
      dataForm: {
        goodsId: undefined,
        goodsName: '',
        unit: undefined,
        expireTime: undefined,
        status: false,
        products: []
      },

      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      query: {
        goodsName: ''
      },
      rules: {
        name: [{ required: true, message: '类目名不能为空', trigger: 'blur' }],
        goodsName: [{ required: true, message: '请输入商品名', trigger: 'blur' }],
        expireTime: [{ required: true, message: '请选择过期时间', trigger: 'blur' }],
        status: [{ required: true, message: '请选择是否启用', trigger: 'blur' }]
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
    this.getList()
  },
  methods: {

    queryGoods(queryString, cb) {
      if (queryString != null) {
        queryGoods({ keywords: this.dataForm.goodsName })
          .then(response => {
            const data = response.data.data.list
            for (const ex of data) {
              ex.value = ex.name // ps:必须为每个对象增加value字段！！因为autocomplete只识别value字段并在下拉列中显示
            }
            cb(data)
          }).catch(response => {
            this.$notify.error({
              title: '查询失败',
              message: response.data
            })
          })
      } else {
        this.$notify.error({
          title: '填写文本',
          message: '查询出错'
        })
      }
    },
    handleGoodsSelected(item) {
      this.dataForm.unit = item.unit
      this.dataForm.picUrl = item.picUrl
      this.dataForm.goodsId = item.id
      queryGoodsProduct({ 'goodsId': this.dataForm.goodsId })
        .then(response => {
          const data = response.data.data.list
          for (const ex of data) {
            ex.rule = 0
            ex.enabled = true
          }
          this.dataForm.products = data
        }).catch(response => {
          this.$notify.error({
            title: '匹配失败',
            message: response.data
          })
        })
    },

    getList() {
      this.listLoading = true
      listRules(this.query)
        .then(response => {
          this.list = response.data.data.list
          // this.dataForm = this.list;
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.listLoading = false
        })
    },

    resetForm() {
      this.dataForm = {
        id: undefined,
        goodsName: '',
        unit: undefined,
        picUrl: undefined,
        expireTime: undefined,
        status: false,
        products: []
      }
    },

    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    createData() {
      this.$refs['dataForm'].validate(valid => {
        const products = this.dataForm.products.filter(product => product.enabled)
        if (valid) {
          createRule({
            products: products,
            enable: this.dataForm.status,
            goodsId: this.dataForm.goodsId,
            expireTime: this.dataForm.expireTime
          })
            .then(response => {
              this.list.unshift(response.data.data)
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
    handleUpdate(row) {
      this.dataForm.unit = row.unit
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          // const products = this.dataForm.products.filter(product => product.enabled)
          editRule({
            products: this.dataForm.products,
            enable: this.dataForm.status,
            goodsId: this.dataForm.goodsId,
            expireTime: this.dataForm.expireTime
          })
            .then((response) => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新成功'
              })
            })
            .catch(response => {
              console.error(response)
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleDelete(row) {
      deleteRule(row)
        .then(response => {
          this.getList()
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>
