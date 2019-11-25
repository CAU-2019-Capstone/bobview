'use strict'
var fs = require('fs')

module.exports = {
    devServer: {
        host: '0.0.0.0',
        hot: true,
        disableHostCheck: true,
        https: {
            key: fs.readFileSync('/etc/letsencrypt/liv/bobview-cert/privkey.pem'),
            cert: fs.readFileSync('/etc/letsencrypt/liv/bobview-cert/privkey.pem'),
        },
        proxy: {
            '/api': {
                // Forward frontend dev server request for /api to django dev server
                target: 'https://www.bobview.org:8080',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
            }
        }
    },
};