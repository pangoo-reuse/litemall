<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.deliveryName" clearable class="filter-item" style="width: 200px;"
                placeholder="请输入姓名"/>
      <el-button
        v-permission="['GET admin/delivery/list']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找
      </el-button>
      <el-button
        v-permission="['POST admin/delivery/create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加
      </el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中..." border fit highlight-current-row>

      <el-table-column align="center" label="ID" prop="id" sortable/>
      <el-table-column align="center" label="姓名" prop="deliveryName"/>
      <el-table-column align="center" label="手机号" prop="mobile"/>
      <el-table-column align="center" label="配送范围" prop="deliveryRegion"/>
      <el-table-column align="center" label="是否启用" prop="enabled">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'error' ">{{ scope.row.enabled ? '启用' : '不启用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['POST /admin/delivery/update']"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑
          </el-button>
          <el-button
            v-permission="['GET /admin/delivery/delete']"
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
      @pagination="getDeliveryList"
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
        <el-form-item label="姓名" prop="deliveryName">
          <el-input v-model="dataForm.deliveryName" placeholder="请输入真实姓名"/>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="dataForm.mobile" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="配送范围" prop="deliveryRegion">
          <el-input v-model="dataForm.deliveryRegion" placeholder="如:云梦城东"/>
        </el-form-item>

        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="dataForm.enabled" placeholder="请选择">
            <el-option :value="true" label="启用"/>
            <el-option :value="false" label="不启用"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createDelivery">确定</el-button>
        <el-button v-else type="primary" @click="updateDelivery">确定</el-button>
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
    import {getToken} from '@/utils/auth'
    import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
    import {deliveryList, deliveryCreate, deliveryUpdate, deliveryDelete} from '@/api/delivery'


    export default {
        name: 'ConfigDelivery',
        components: {Pagination},
        data() {
            return {
                list: [],
                total: 0,
                listLoading: true,
                listQuery: {
                    page: 1,
                    limit: 10,
                    deliveryName: undefined
                },
                dataForm: {
                    id: undefined,
                    deliveryName: undefined,
                    mobile: undefined,
                    deliveryRegion: undefined,
                    enabled: true
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '创建'
                },
                rules: {
                    deliveryName: [
                        {required: true, message: '不能为空', trigger: 'blur'}
                    ],
                    mobile: [
                        {required: true, message: '不能为空', trigger: 'blur'}
                    ],
                    deliveryRegion: [
                        {required: true, message: '不能为空', trigger: 'blur'}
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
            this.getDeliveryList()
        },
        methods: {
            getDeliveryList() {
                console.log(this.listQuery)
                this.listLoading = true
                deliveryList(this.listQuery).then(response => {
                    this.list = response.data.data.list
                    this.total = response.data.data.total
                    this.listLoading = false
                }).catch(() => {
                    this.list = []
                    this.total = 0
                    this.listLoading = false
                })
            },

            resetForm() {
                this.dataForm = {
                    id: undefined,
                    deliveryName: undefined,
                    mobile: undefined,
                    deliveryRegion: undefined,
                    enabled: true
                }
            },
            handleFilter() {
                this.listQuery.page = 1
                this.getDeliveryList()
            },
            handleCreate() {
                this.resetForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['dataForm'].clearValidate()
                })
            },
            createDelivery() {
                this.$refs['dataForm'].validate(valid => {
                    if (valid) {
                        const dataForm = this.dataForm
                        console.log(dataForm)
                        deliveryCreate(dataForm)
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
            updateDelivery() {
                this.$refs['dataForm'].validate(valid => {
                    if (valid) {
                        deliveryUpdate(this.dataForm)
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
                deliveryDelete({id: row.id})
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

