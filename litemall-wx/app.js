var util = require('./utils/util.js');
var api = require('./config/api.js');
var user = require('./utils/user.js');

App({

  //获取用户地理位置权限
  getPermission: function (obj) {
    wx.getLocation({
      type: 'wgs84',
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        console.log(latitude)
        wx.request({
          url: 'https://apis.map.qq.com/ws/geocoder/v1/?location=' + latitude + "," + longitude + "&key=DFVBZ-M5GKJ-5ZIFN-F36EH-TBBS5-WQFTU",
          success(res) {
            var address = res.data.result.address_component
            var province = address.province;
            var city = address.city;
            var district = address.district;
            //  "province": "湖北省",
            //   "city": "孝感市",
            //   "district": "孝南区",
            util.request(api.MatchArea, {
              'province': province,
              'city': city,
              'district': district
            }).then(function (res) {
              console.log(res)
            });
            console.log(address)
          }
        })

        obj.setData({
          latitude: latitude,
          longitude: longitude
        })
      }, fail: function () {
        wx.getSetting({
          success: function (res) {
            var auth = res.authSetting;
            if (!auth['scope.userLocation']) {
              wx.showModal({
                title: '是否授权当前位置？',
                content: '获取您的位置以便以我们为您匹配优惠好物',
                success: function (tip) {
                  if (tip.confirm) {
                    wx.openSetting({
                      success: function (data) {
                        if (data.authSetting["scope.userLocation"] === true) {
                          wx.showToast({
                            title: '授权成功',
                            icon: 'success',
                            duration: 1000
                          })
                          //授权成功之后，再调用chooseLocation选择地方
                          wx.getLocation({
                            type: 'wgs84',
                            success(res) {
                              const latitude = res.latitude
                              const longitude = res.longitude
                              obj.setData({
                                latitude: latitude,
                                longitude: longitude
                              })
                            }
                          })
                        } else {
                          wx.showToast({
                            title: '授权失败',
                            icon: 'success',
                            duration: 1000
                          })
                        }
                      }
                    })
                  }
                }
              })
            }
          },
          fail: function (res) {
            wx.showToast({
              title: '调用授权窗口失败',
              icon: 'success',
              duration: 1000
            })
          }
        })
      }
    })

  },

  onLaunch: function () {
    const updateManager = wx.getUpdateManager();
    wx.getUpdateManager().onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success: function (res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            updateManager.applyUpdate()
          }
        }
      })
    })
  },
  onShow: function (options) {
    user.checkLogin().then(res => {
      this.globalData.hasLogin = true;
    }).catch(() => {
      this.globalData.hasLogin = false;
    });
  },
  globalData: {
    hasLogin: false,
    address: undefined
  }
})