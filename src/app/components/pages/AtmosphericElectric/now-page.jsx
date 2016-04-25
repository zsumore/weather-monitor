import React from 'react';
import request from 'superagent/lib/client';
import echarts from 'echarts/echarts-line-map';


import { ClearFix } from 'material-ui';
import { StylePropable, StyleResizable } from 'material-ui/lib/mixins';
import Table from 'material-ui/lib/table/table';
import TableHeaderColumn from 'material-ui/lib/table/table-header-column';
import TableRow from 'material-ui/lib/table/table-row';
import TableHeader from 'material-ui/lib/table/table-header';
import TableRowColumn from 'material-ui/lib/table/table-row-column';
import TableBody from 'material-ui/lib/table/table-body';
import Colors from 'material-ui/lib/styles/colors';
import Checkbox from 'material-ui/lib/checkbox';

import CommunicationLiveHelp from 'material-ui/lib/svg-icons/communication/live-help';
import ContentLink from 'material-ui/lib/svg-icons/content/link';


import NavigationRefresh from 'material-ui/lib/svg-icons/navigation/refresh';
import NavigationChevronLeft from 'material-ui/lib/svg-icons/navigation/chevron-left';
import NavigationChevronRight from 'material-ui/lib/svg-icons/navigation/chevron-right';
import NavigationArrowBack from 'material-ui/lib/svg-icons/navigation/arrow-back';
import NavigationArrowForward from 'material-ui/lib/svg-icons/navigation/arrow-forward';
import IconButton from 'material-ui/lib/icon-button';


import { Box } from 'react-layout-components/lib';

import DateTimeField from 'react-bootstrap-datetimepicker';
import moment from 'moment';
import TimerMixin from 'react-timer-mixin';

import NowMapChartOption from './now-map-chart-option';
import NowLineChartOption from './now-line-chart-option';
import { AEStationSet, AEStationCoordMap, AEStationNameMap } from './ae-station-option';



const getMapChartHeight = () => {
    let _height = window.innerHeight - 250;
    if (_height >= 510) {
        return _height;
    } else {
        return 510;
    }
};

const getLineChartWidth = (isClose) => {
    if (isClose)
        return window.innerWidth - getMapChartHeight();
    return window.innerWidth - 280 - getMapChartHeight();
};

const convertData = (data) => {

    let res = [];
    for (let i = 0; i < data.length; i++) {

        let geoCoord = AEStationCoordMap[data[i].sid];

        if (geoCoord) {

            let geoStaion = AEStationNameMap[data[i].sid];
            if (geoStaion) {

                res.push({
                    name: data[i].sid,
                    value: geoCoord.concat(data[i].value)
                });
            }
        }
    }

    return res;
};

const convertMapChartData = (data) => {

    let res = [];
    for (let i = 0; i < data.length; i++) {

        let geoCoord = AEStationCoordMap[data[i].stationid];

        if (geoCoord) {

            let geoStaion = AEStationNameMap[data[i].stationid];
            if (geoStaion) {

                res.push({
                    name: data[i].stationid,
                    value: geoCoord.concat(data[i].value)
                });
            }
        }
    }

    return res;
};


const getMapChartData = (data) => {
    let res = [];
    for (let i = 0; i < data.length; i++) {
        if (data[i].value[2] > 20) {
            res.push(data[i]);
        }
    }
    return res;
};

const getAELineChartxAxisData = (data) => {
    let res = [];
    for (let i = 0; i < data.length; i++) {

        res.push(data[i].dt);

    }

    return res;
};

const getAELineChartData = (data) => {
    let res = [];
    for (let i = 0; i < data.length; i++) {

        res.push(data[i].intensity);

    }
    //console.log(res);
    return res;
};


const getNormalData = (data) => {
    let res = [];
    for (let i = 0; i < data.length; i++) {
        if (data[i].value[2] <= 20) {
            res.push(data[i]);
        }
    }
    return res;
};

const getNowTime = () => {

    return moment().seconds(0).milliseconds(0);
};


const iconStyles = {
    marginTop: 8,
    marginRight: 24,
};



const AtmosphericElectricNowPage = React.createClass({

    propTypes: {
        onChangeMuiTheme: React.PropTypes.func,
        isLeftNavClose: React.PropTypes.bool

    },

    contextTypes: {
        muiTheme: React.PropTypes.object
    },

    mixins: [StylePropable, StyleResizable, TimerMixin],

    getInitialState() {
        return {
            leftNavClose: true,
            mapChart: {},
            mapChartOption: NowMapChartOption,
            mapChartHeight: getMapChartHeight(),
            lineChart: {},
            lineChartOption: NowLineChartOption,
            lineChartWidth: getLineChartWidth(true),

            dateTime: getNowTime(),
            auto: true,
            mapChartData: {
                data: []
            },
            timer: null,
            selectedStation: '59828'
        };
    },




    handleChangeMapChartData(dt) {

        const _page = this;

        request.post('http://10.151.78.189:8080/monitor/servlet/MapServlet')
            .type('form')
            .send({
                start: dt.format('YYYY-MM-DD HH:mm:ss')
            }).timeout(5000).end((err, res) => {
            let _mapChartData = JSON.parse(res.text);

            _page.state.mapChartOption.title.subtext = _mapChartData.datetime;
            let _tempset = new Set();
            for (let i = 0; i < _mapChartData.data.length; i++) {
                _tempset.add(_mapChartData.data[i].stationid);
            }

            for ( let x of AEStationSet ) {

                if (_tempset.has(x)) {

                } else {
                    let _tempdata = {
                        'last_time': '无数据',
                        'value': -999,
                        'stationid': x
                    };
                    _mapChartData.data.push(_tempdata);
                }
            }

            _mapChartData.data.sort((o1, o2) => {
                return o2.value - o1.value;
            });

            _page.setState({
                mapChartData: _mapChartData
            });

            _page.state.mapChartOption.series[0].data = getNormalData(convertMapChartData(_mapChartData.data));
            _page.state.mapChartOption.series[1].data = getMapChartData(convertMapChartData(_mapChartData.data));

            _page.state.mapChart.clear();
            _page.state.mapChart.setOption(_page.state.mapChartOption);

        });

    },
    handleChangeMapChartOption(option) {

        mapChart.setOption(option);

    },

    handleChangeLineChartOption(stationid, dt, option) {

        const _state = this.state;
        if (option) {
            _state.lineChart.setOption(option);
        } else {

            request.post('http://10.151.78.189:8080/monitor/servlet/MinuteMeteoServlet')
                .type('form')
                .send({
                    stationid: stationid,
                    start: dt.format('YYYY-MM-DD HH:mm:ss')
                }).timeout(5000).end((err, res) => {

                let _valueData = JSON.parse(res.text);

                let _lineChartOption = _state.lineChartOption;
                _lineChartOption.xAxis[0].data = getAELineChartxAxisData(_valueData.data).map((str) => {
                    return str.replace(' ', '\n')
                });
                _lineChartOption.series[0].data = getAELineChartData(_valueData.data);
                _state.lineChart.clear();
                _state.lineChart.setOption(_lineChartOption);

            });

        }

    },

    handleTableRowSelection(rowArray) {


        this.handleChangeStation(this.state.mapChartData.data[rowArray[0]].stationid);
    },
    handleTableRowSelected(sid) {
        return sid === this.state.selectedStation;

    },

    handleChangeTimeByMinute(param) {


        let _time = moment(this.state.dateTime);

        _time.minutes(_time.minutes() + param);

        this.setState({
            dateTime: _time
        });

        this.handleChangeDatetime(_time);

    },

    handleRefreshTime() {

        let _time = getNowTime();

        this.setState({
            dateTime: _time
        });

        this.handleChangeDatetime(_time);

    },
    handleChangeDatetime(_time) {

        this.handleChangeLineChartOption(this.state.selectedStation, _time);
        this.handleChangeMapChartData(_time);
    },

    handleChangeDateTimeField(event) {

        let _time = moment(event);

        this.setState({
            dateTime: _time
        });

        this.handleChangeDatetime(_time);

    },

    handleChangeStation(sid) {

        if (this.state.selectedStation !== sid) {

            let _name = AEStationNameMap[sid];
            if (_name) {

                this.state.lineChartOption.title.subtext = _name;
                this.handleChangeLineChartOption(sid, this.state.dateTime);

                this.setState({
                    selectedStation: sid
                });
            }
        }

    },

    handleCheckBoxChange(e, isInputChecked) {
        if (isInputChecked) {
            if (this.state.timer) {
                this.clearInterval(this.state.timer);
            }

            this.state.timer = this.setInterval(this.handleRefreshTime, 60000);
        } else {
            if (this.state.timer) {
                this.clearInterval(this.state.timer);
            }
        }
    },

    componentWillReceiveProps(nextProps, nextContext) {
        const newMuiTheme = nextContext.muiTheme ? nextContext.muiTheme : this.state.muiTheme;
        this.setState({
            muiTheme: newMuiTheme,
            leftNavClose: this.props.isLeftNavClose !== nextProps.isLeftNavClose ? nextProps.isLeftNavClose : this.state.leftNavClose

        });

        this.state.lineChart.resize();
    },


    componentDidMount() {
        const _page = this;

        _page.state.mapChart = echarts.init(document.getElementById('AtmosphericElectricHourPage.mapChart'));

        _page.state.mapChart.on('click', function(param) {
            _page.handleChangeStation(param.name);

        });

        request.get('map/json/440600.json').timeout(5000)
            .end((err, res) => {
                echarts.registerMap('foshan', res.text);

                _page.handleChangeMapChartData(_page.state.dateTime);
            });

        _page.state.lineChart = echarts.init(document.getElementById('AtmosphericElectricHourPage.lineChart'));
        _page.handleChangeLineChartOption('59828', _page.state.dateTime);

        _page.state.timer = _page.setInterval(_page.handleRefreshTime, 60000);
    },

    render() {
        console.log('render');
        return (
            <div>
            <Box className='page-title'  justifyContent='space-between' alignItems='baseline'>
            <Box   justifyContent='space-between' alignItems='baseline'>
            <h3>大气电场即时数据</h3>
            <a className={ 'hint--top-right' } data-hint={ '数据查询' } href="http://10.151.64.202:8097/home" target="_blank">
            <ContentLink style={{
                marginLeft: 10
            }} />
            />   
            </a>
            </Box>
            <div className={ 'hint--top-left' } data-hint={ '使用说明' }>
            <CommunicationLiveHelp  style={iconStyles} color={Colors.blue500} hoverColor={Colors.greenA200} onClick={() => alert('click help')} />
            </div>
            </Box>
            <Box width='100%'  justifyContent='space-around' alignItems='flex-start'  column={false} reverse={false}>
            <Box  flex={1} style={{
                width: this.state.lineChartWidth,
                height: this.state.mapChartHeight + 50
            }} column>
             <Box id='AtmosphericElectricHourPage.lineChart'  style={{
                width: '95%',
                height: (this.state.mapChartHeight + 50) * 0.6
            }} >
           </Box>
            <Box style={{
                width: '95%',
                height: (this.state.mapChartHeight + 50) * 0.4,
                marginRight: 50
            }}>
          <Table
            width={'100%'}
            fixedHeader={false}
            selectable={true}
            multiSelectable={false}
            onRowSelection={this.handleTableRowSelection}
            >
          <TableHeader enableSelectAll={false}>
            <TableRow>
              <TableHeaderColumn colSpan="3" tooltip="Super Header" style={{
                textAlign: 'center'
            }}>
                {this.state.dateTime.format('YYYY-MM-DD HH:mm:ss')}
              </TableHeaderColumn>
            </TableRow>
            <TableRow>
              <TableHeaderColumn tooltip="站名">站名</TableHeaderColumn>
              <TableHeaderColumn tooltip="预警值">预警值</TableHeaderColumn>
              <TableHeaderColumn tooltip="最后更新时间">更新时间</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody
            deselectOnClickaway={false}
            showRowHover={true}
            stripedRows={true}
            >
            {this.state.mapChartData.data.map((row, index) => (
            <TableRow key={row.stationid} selected={this.handleTableRowSelected(row.stationid)}>
                <TableRowColumn>{AEStationNameMap[row.stationid]}</TableRowColumn>
                <TableRowColumn>{row.value}</TableRowColumn>
                <TableRowColumn>{row.last_time}</TableRowColumn>
              </TableRow>
            ))}
            </TableBody>
            </Table>
            </Box>
            </Box>
            <Box style={{
                width: this.state.mapChartHeight,
                height: this.state.mapChartHeight + 50
            }} column>
            <Box width='100%' justifyContent='space-between'>
            <Box alignItems='center' style={{
                height: 50
            }}>
            <IconButton tooltip="-10分钟" tooltipPosition='top-center'    onClick={this.handleChangeTimeByMinute.bind(this, -10)} >
            <NavigationArrowBack  />
            </IconButton>
            <IconButton tooltip="-5分钟"  tooltipPosition='top-center'   onClick={this.handleChangeTimeByMinute.bind(this, -5)}>
            <NavigationChevronLeft  />
            </IconButton>
            <div style={{
                width: 160
            }}>
            <DateTimeField   onChange={this.handleChangeDateTimeField} format='YYYY-MM-DD HH:mm:ss' inputFormat='MM-DD HH:mm' dateTime={this.state.dateTime.format('YYYY-MM-DD HH:mm:ss')} />
            </div>
            <IconButton tooltip="+5分钟" tooltipPosition='top-center'   onClick={this.handleChangeTimeByMinute.bind(this, 5)}>
            <NavigationChevronRight />
            </IconButton>
            <IconButton tooltip="+10分钟"  tooltipPosition='top-center'   onClick={this.handleChangeTimeByMinute.bind(this, 10)}>
            <NavigationArrowForward />
            </IconButton>
            <IconButton tooltip="刷新"  tooltipPosition='top-center'  onClick={this.handleRefreshTime}>
            <NavigationRefresh color={Colors.green500} />
            </IconButton>
            </Box>
            <Box alignItems='center' style={{
                height: 50,
                width: 120
            }}>
            <Checkbox
            label="更新"
            defaultChecked={this.state.auto}
            onCheck={this.handleCheckBoxChange}
            />
            </Box>
            </Box>
            <Box id='AtmosphericElectricHourPage.mapChart'   style={{
                width: this.state.mapChartHeight,
                height: this.state.mapChartHeight
            }} />
            </Box>
            </Box>
        </div>);
    }
});

export default AtmosphericElectricNowPage;
