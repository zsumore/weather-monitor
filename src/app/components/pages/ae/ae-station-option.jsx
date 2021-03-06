import React from 'react';
import 'babel-polyfill';

export const AEStationArray = ['AE2224', 'AE6946', 'AE6949', 'AE2229', '59288', 'AE6834', 'AE2262', 'AE6963', 'AE2270', 'AE2213', 'AE2267', '59828', 'AE2264', 'AE7019', 'AE7032', 'AE7031'];

//export const AEStationSet = new Set(['59828', 'AE2267', 'AE2213', 'AE6834', 'AE2264', 'AE2262', 'AE2270', 'AE2229', 'AE6963', 'AE6946', 'AE2224', 'AE6949', 'AE7019', 'AE7032', 'AE7031', '59288']);
export const AEStationSet = new Set(AEStationArray);
export const AEStationCoordMap = {
    'AE2224': [112.855, 23.5058],
    'AE6946': [112.9094, 23.3811],
    'AE6949': [112.9728, 23.3078],
    'AE2229': [112.867, 23.183],
    '59288': [113.0086, 23.1447],
    'AE6834': [113.1389, 23.1333],
    'AE2262': [113, 23.117],
    'AE6963': [112.8631, 23.0717],
    'AE2270': [113.0483, 23.0619],
    'AE2213': [113.113, 23.05],
    'AE2267': [113.1333, 23.0336],
    '59828': [113.115, 23.0145],
    'AE2264': [112.9997, 22.9494],
    'AE7019': [112.895, 22.8886],
    'AE7032': [112.7142, 22.8631],
    'AE7031': [112.4508, 22.7497]
};

export const AEStationNameMap = {
    '59828': '佛山气象局（季华）',
    'AE2267': '南海区三防办',
    'AE2213': '南海气象局测站(桂城)',
    'AE6834': '南海雅瑶小学',
    'AE2264': '南海官山大泵站',
    'AE2262': '南海官窑水利所',
    'AE2270': '南海罗村水利所',
    'AE2229': '三水气象局',
    'AE6963': '三水白泥小学',
    'AE6946': '三水上塘村委',
    'AE2224': '三水迳口南山小学',
    'AE6949': '三水范湖工业园',
    'AE7019': '高明气象局',
    'AE7032': '高明明城三防中心',
    'AE7031': '高明更合镇吉田村委会',
    '59288': '南海狮山测站'
};






