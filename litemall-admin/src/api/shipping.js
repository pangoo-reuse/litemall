import request from '@/utils/request'

export function shippingList(query) {
  return request({
    url: '/shipping/list',
    method: 'get',
    params: query
  })
}

export function shippingSearch(query) {
  return request({
    url: '/shipping/search',
    method: 'get',
    params: query
  })
}

export function shippingCreate(data) {
  return request({
    url: '/shipping/create',
    method: 'post',
    data
  })
}

export function shippingUpdate(data) {
  return request({
    url: '/shipping/update',
    method: 'post',
    data
  })
}

export function shippingDelete(query) {
  return request({
    url: '/shipping/delete',
    method: 'get',
    params: query
  })
}

