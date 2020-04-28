import request from '@/utils/request'

export function listOrders(query) {
  return request({
    url: '/p2p/orderList',
    method: 'get',
    params: query
  })
}
export function queryGoods(query) {
  return request({
    url: '/p2p/queryGoods',
    method: 'get',
    params: query
  })
}
export function queryGoodsProduct(query) {
  return request({
    url: '/p2p/queryGoodsProduct',
    method: 'get',
    params: query
  })
}

export function listRules(query) {
  return request({
    url: '/p2p/list',
    method: 'get',
    params: query
  })
}


export function deleteRule(data) {
  return request({
    url: '/p2p/delete',
    method: 'post',
    data
  })
}

export function createRule(data) {
  return request({
    url: '/p2p/create',
    method: 'post',
    data
  })
}

export function editRule(data) {
  return request({
    url: '/p2p/update',
    method: 'post',
    data
  })
}
