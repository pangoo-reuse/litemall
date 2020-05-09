import request from '@/utils/request'

export function deliveryList(query) {
  return request({
    url: '/delivery/list',
    method: 'get',
    params: query
  })
}


export function deliveryCreate(data) {
  return request({
    url: '/delivery/create',
    method: 'post',
    data
  })
}

export function deliveryUpdate(data) {
  return request({
    url: '/delivery/update',
    method: 'post',
    data
  })
}

export function deliveryDelete(query) {
  return request({
    url: '/delivery/delete',
    method: 'get',
    params: query
  })
}

